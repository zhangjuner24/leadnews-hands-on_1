package com.heima.media.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.common.dto.ResponseResult;
import com.heima.common.minio.MinIOService;
import com.heima.common.util.SensitiveWordUtil;
import com.heima.media.client.ArticleFeign;
import com.heima.media.dto.ArticleDto;
import com.heima.media.dto.ContentDto;
import com.heima.media.dto.ImageDto;
import com.heima.media.entity.WmChannel;
import com.heima.media.entity.WmNews;
import com.heima.media.entity.WmSensitive;
import com.heima.media.entity.WmUser;
import com.heima.media.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuditServiceImpl  implements IAuditService {
    @Autowired
    private IWmUserService wmUserService;
    @Autowired
    private IWmChannelService wmChannelService;
    @Autowired
    private ArticleFeign articleFeign;
    @Autowired
    private IWmNewsService wmNewsService;
    @Autowired
    private GreenTextScan greenTextScan;
    @Autowired
    private GreenImageScan greenImageScan;
    @Autowired
    private MinIOService minIOService;

    @Override
    public void audit(Integer newsId) {
        WmNews wmNews = wmNewsService.getById(newsId);
        audit(wmNews);
    }

    @Override
    @Async("asyncExecutor")
    public void audit(WmNews wmNews) {
        log.error("执行audit方法的线程名称："+Thread.currentThread().getName());
        //1、 自管理敏感词审核  DFA的算法
        boolean isPass = checkSensitive(wmNews);
        if(!isPass)return;

        // 2、阿里云文本审核  TODO 正式运行时可以放开
        // isPass = checkText(wmNews);
        // if(!isPass)return;

        // 3、阿里云图片审核  TODO 正式运行时可以放开
        // isPass = checkImage(wmNews);
        // if(!isPass)return;

        // 4、远程调用新增app文章
        Long articleId = creatApArticle(wmNews); //远程调用 先 app文章表中新增数据
        wmNews.setArticleId(articleId);
        // 0 草稿  1 提交（待审核）  2 审核失败 3 人工审核   4 人工审核通过  9 已发布
        wmNews.setStatus(9);
        wmNews.setSubmitedTime(new Date());
        wmNewsService.updateById(wmNews);

    }


    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private IWmSensitiveService wmSensitiveService;

    private boolean checkSensitive(WmNews wmNews) {

        // 1、查询所有的敏感词 初始化一个map
        List<WmSensitive> list = wmSensitiveService.list();
        List<String> collect = list.stream().map(s -> {
            return s.getSensitives();
        }).collect(Collectors.toList());
        SensitiveWordUtil.initMap(collect);

        // 2、审核
        String text = wmNews.getTitle();
        List<ContentDto> contentDtos = JSON.parseArray(wmNews.getContent(), ContentDto.class);

        String value = contentDtos.stream().map(contentDto -> {
            if (contentDto.getType().equals("text")) {
                return contentDto.getValue();
            }
            return "";
        }).collect(Collectors.joining(","));

        Map<String, Integer> integerMap = SensitiveWordUtil.matchWords(text + value);

        //  integerMap： {小额贷款=3, 随机随贷=1, 无抵押=1}

        if (integerMap.size()==0) {
            return true;
        }

        wmNews.setStatus(2); //审核失败

        Set<String> strings = integerMap.keySet();
        String reason = StringUtils.join(strings, ",");
        wmNews.setReason("自管理敏感词文本审核失败，原因是包含如下敏感词："+reason);
        wmNewsService.updateById(wmNews);
        return false;

    }

    private boolean checkImage(WmNews wmNews) {
        // 1、根据url先去重
        // 先获取内容图片URL
        Set<String> urlSet = getContentImageFromContent(wmNews.getContent());
        // 再获取封面图片URL
        List<ImageDto> imageDtoList = JSON.parseArray(wmNews.getImages(), ImageDto.class);

        for (ImageDto imageDto : imageDtoList) {
            urlSet.add(imageDto.getUrl());
        }


        List<byte[]> imageList = new ArrayList<>();

        try {
            // 2、需要从minio中下载
            for (String url : urlSet) {
                if(StringUtils.isNotBlank(url)){
                 InputStream inputStream = minIOService.download(url);
                 imageList.add(IOUtils.toByteArray(inputStream));
                }
            }
            // 3、阿里云审核
            Map<String,String> map = greenImageScan.imageScan(imageList);
            String suggestion = (String) map.get("suggestion");
            // suggestion: pass review  block
            switch (suggestion){
                case "pass":{
                    return true;
                }
                case "review":{

                    wmNews.setStatus(3); //需要人工审核
                    wmNews.setReason("阿里云图片审核需要人工审核，原因是："+(String) map.get("label"));
                    wmNewsService.updateById(wmNews);
                    return false;
                }
                case "block":{
                    wmNews.setStatus(2); //审核失败
                    wmNews.setReason("阿里云图片审核失败，原因是："+(String) map.get("label"));
                    wmNewsService.updateById(wmNews);
                    return false;
                } default:
                    return false;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Set<String> getContentImageFromContent(String content) {
        List<ContentDto> contentDtos = JSON.parseArray(content, ContentDto.class);

        return contentDtos.stream().map(contentDto -> {
            if (contentDto.getType().equals("image")) {
                return contentDto.getValue();
            }
            return "";
        }).collect(Collectors.toSet());

    }

    private boolean checkText(WmNews wmNews) {
        // 审核标题和内容文本
        String text = wmNews.getTitle();
        List<ContentDto> contentDtos = JSON.parseArray(wmNews.getContent(), ContentDto.class);

        String value = contentDtos.stream().map(contentDto -> {
            if (contentDto.getType().equals("text")) {
                return contentDto.getValue();
            }
            return "";
        }).collect(Collectors.joining(","));

        try {
            Map<String,String> map = greenTextScan.greenTextScan(text + value);
            String suggestion = (String) map.get("suggestion");
            // suggestion: pass review  block
            switch (suggestion){
                case "pass":{
                    return true;
                }
                case "review":{

                    wmNews.setStatus(3); //需要人工审核
                    wmNews.setReason("阿里云文本审核需要人工审核，原因是："+(String) map.get("label"));
                    wmNewsService.updateById(wmNews);
                    return false;
                }
                case "block":{
                    wmNews.setStatus(2); //审核失败
                    wmNews.setReason("阿里云文本审核失败，原因是："+(String) map.get("label"));
                    wmNewsService.updateById(wmNews);
                    return false;
                } default:
                    return false;

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }


    public Long creatApArticle(WmNews wmNews) {

        if(wmNews.getId() != null){
            wmNews = wmNewsService.getById(wmNews.getId());
        }

        ArticleDto articleDto = new ArticleDto();
        // TODO  任务2.1：给articleDto赋值

        ResponseResult<Long> responseResult = articleFeign.saveApArticle(articleDto);
        return responseResult.getData();

    }
}
