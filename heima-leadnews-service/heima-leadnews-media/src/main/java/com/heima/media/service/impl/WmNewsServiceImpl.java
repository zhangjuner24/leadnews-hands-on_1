package com.heima.media.service.impl;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.dto.PageResponseResult;
import com.heima.common.dto.ResponseResult;
import com.heima.common.dto.User;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.util.UserThreadLocalUtil;
import com.heima.media.dto.ContentDto;
import com.heima.media.dto.ImageDto;
import com.heima.media.dto.WmNewsDto;
import com.heima.media.dto.WmNewsPageDto;
import com.heima.media.entity.WmNews;
import com.heima.media.entity.WmNewsMaterial;
import com.heima.media.mapper.WmNewsMapper;
import com.heima.media.service.IAuditService;
import com.heima.media.service.IWmNewsMaterialService;
import com.heima.media.service.IWmNewsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * <p>
 * 自媒体图文内容信息表 服务实现类
 * </p>
 *
 * @author syl
 * @since 2023-04-18
 */
@Service
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements IWmNewsService {

    @Autowired
    private IWmNewsMaterialService wmNewsMaterialService;
    @Autowired
    @Lazy
    private IAuditService auditService;



    @Override
    public ResponseResult listByCondition(WmNewsPageDto dto) {
        User user = UserThreadLocalUtil.get();
        if(user==null){ //必须登录后才能操作此方法
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        LambdaQueryWrapper<WmNews> queryWrapper = new LambdaQueryWrapper<>();
        // 1、状态
        if(dto.getStatus()!=null){
            queryWrapper.eq(WmNews::getStatus,dto.getStatus());
        }
        // 2、关键字
        if(StringUtils.isNotBlank(dto.getKeyword())){
            queryWrapper.like(WmNews::getTitle,dto.getKeyword());
        }
        // 3、频道
        if(dto.getChannelId()!=null){
            queryWrapper.eq(WmNews::getChannelId,dto.getChannelId());
        }
        // 4、日期 开始日期 结束日期
        if(dto.getBeginPubDate()!=null&&dto.getEndPubDate()!=null){
            // getBeginPubDate       WmNews::getPublishTime;  getEndPubDate
            queryWrapper.ge( WmNews::getPublishTime,dto.getBeginPubDate());
            queryWrapper.le( WmNews::getPublishTime,dto.getEndPubDate());
        }
        // 5、userId
        queryWrapper.eq(WmNews::getUserId,user.getUserId());
        // 6、发布时间排序
        queryWrapper.orderByDesc(WmNews::getPublishTime);

        // 7、分页
        Page<WmNews> page = new Page<WmNews>(dto.getPage(),dto.getSize());

        Page<WmNews> page1 = this.page(page, queryWrapper);
        return new PageResponseResult(dto.getPage(),dto.getSize(),page1.getTotal(),page1.getRecords());
    }

    @Override
    @Transactional
    public ResponseResult submit(WmNewsDto dto) {
        User user = UserThreadLocalUtil.get();
        if(user==null){ //必须登录后才能操作此方法
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        //TODO  任务2.2，完成自媒体文章的保存



        return null;
    }

    @Override
    public ResponseResult downOrUp(WmNewsDto dto) {
        User user = UserThreadLocalUtil.get();
        if(user==null){ //必须登录后才能操作此方法
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        Integer id = dto.getId();
        WmNews wmNews = this.getById(id);
        if(wmNews==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }

        dto.getEnable();//0:下架   1：上架

        wmNews.setEnable(dto.getEnable()==1);   // true--1  false---0

        this.updateById(wmNews);

        // 向Kafka发送消息  update ap_article SET is_down = ? WHERE id=?

        Map map = new HashMap();
        map.put("articleId",wmNews.getArticleId());
        map.put("isDown",dto.getEnable()==0); // 要和Enable的值取反

        kafkaTemplate.send(upDownTopic,JSON.toJSONString(map));

        return ResponseResult.okResult();
    }
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Value("${topic.upDownTopic}")  //从yml中获取
    private String upDownTopic;




    private void saveRelation( List<ImageDto> imageDtoList ,int type,Integer newsId){  //0:内容图片  1：封面图片
        int i =0 ;
        for (ImageDto imageDto : imageDtoList) {
            WmNewsMaterial wmNewsMaterial = new WmNewsMaterial();
            wmNewsMaterial.setMaterialId(imageDto.getId());
            wmNewsMaterial.setNewsId(newsId);
            wmNewsMaterial.setType(type);
            wmNewsMaterial.setOrd(i);
            i++;
            wmNewsMaterialService.save(wmNewsMaterial);
        }
    }

    private List<ImageDto> getContentImagesFromContent(String content) {
       /* "content":"[
        {
            "type":"text",
                "value":"随着智能手机的普及，人们更加习惯于通过手机来看新闻。由于生活节奏的加快，很多人只能利用碎片时间来获取信息，因此，对于移动资讯客户端的需求也越来越高。黑马头条项目正是在这样背景下开发出来。黑马头条项目采用当下火热的微服务+大数据技术架构实现。本项目主要着手于获取最新最热新闻资讯，通过大数据分析用户喜好精确推送咨询新闻"
        },
        {
            "type":"image",
                "value":"http://192.168.200.130/group1/M00/00/00/wKjIgl5swbGATaSAAAEPfZfx6Iw790.png",
                "id" :176
        }*/

        List<ImageDto> imageDtoList = new ArrayList<>();

        List<ContentDto> contentDtos = JSON.parseArray(content, ContentDto.class);
        for (ContentDto contentDto : contentDtos) {
            if (contentDto.getType().equals("image")) {
                ImageDto imageDto = new ImageDto();
                imageDto.setId(contentDto.getId());
                imageDto.setUrl(contentDto.getValue());
                imageDtoList.add(imageDto);
            }
        }
        return imageDtoList;

    }


    private void deleteRelation(Integer newsId) {
        // delete from wm_news_material where news_id=??
        LambdaQueryWrapper<WmNewsMaterial> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WmNewsMaterial::getNewsId,newsId);
        wmNewsMaterialService.remove(queryWrapper);
    }
}
