package com.heima.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.dto.ArticleDto;
import com.heima.article.dto.ArticleHomeDto;
import com.heima.article.dto.ArticleInfoDto;
import com.heima.article.dto.ArticleStreamMessage;
import com.heima.article.entity.ApArticle;
import com.heima.article.entity.ApArticleContent;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.IApArticleContentService;
import com.heima.article.service.IApArticleService;
import com.heima.article.task.CreateHtmlTask;
import com.heima.common.dto.ResponseResult;
import com.heima.common.enums.AppHttpCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 文章信息表，存储已发布的文章 服务实现类
 * </p>
 *
 * @author syl
 * @since 2023-04-19
 */
@Service
@Slf4j
// @Scope("singleton")  prototype  request  session globalSession
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements IApArticleService {

    @Autowired
    private IApArticleContentService apArticleContentService;
   // @Autowired
   //  private CreateHtmlTask createHtmlTask;
   @Autowired
   private StringRedisTemplate redisTemplate;

    @Override
    public ResponseResult<Long> saveApArticle(ArticleDto dto) {
        // 判断是否有ID   有就是修改   没有就是新增
        ApArticle article = new ApArticle();
        BeanUtils.copyProperties(dto,article);

        if(dto.getId()==null){

            article.setCreatedTime(new Date());
            this.save(article);  //保存后就有id

            ApArticleContent articleContent = new ApArticleContent();
            articleContent.setArticleId(article.getId());
            articleContent.setContent(dto.getContent());
            apArticleContentService.save(articleContent);

            // createHtmlTask.createHtml(article,dto.getContent());

            return ResponseResult.okResult(article.getId());

        }else{

            ApArticle apArticle = this.getById(dto.getId());
            if(apArticle==null){
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
            }

            this.updateById(article);

            // update ap_article_content set content=? where article_id=??
            LambdaUpdateWrapper<ApArticleContent> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(ApArticleContent::getContent,dto.getContent());
            updateWrapper.eq(ApArticleContent::getArticleId,dto.getId());
            apArticleContentService.update(updateWrapper);

            // createHtmlTask.createHtml(article,dto.getContent());

            return ResponseResult.okResult(article.getId());
        }


    }

    @Override
    public ResponseResult load(ArticleHomeDto dto, int type) { //  1:第一次加载   2：加载更多  3：加载更新

        Integer size = dto.getSize();
        if (size==null) {
            size = 10;
        }

        List<ApArticle> apArticleList = new ArrayList<>();
        // 查询缓存
        if(dto.getLoaddir()==1){
            Set<String> strings = redisTemplate.boundZSetOps("hot_article_first_page_" + dto.getChannelId()).reverseRange(0, size);
            for (String string : strings) {
                ApArticle apArticle = JSON.parseObject(string, ApArticle.class);
                apArticleList.add(apArticle);
            }
            // 如果每页显示10条数 但是这里你只查询到3条
            if(apArticleList.size()>=size){
                return ResponseResult.okResult(apArticleList);
            }
        }

        // 如果缓存没有，查数据库

        LambdaQueryWrapper<ApArticle> queryWrapper = new LambdaQueryWrapper<>();

        // 有两个隐藏条件 已上架  未删除
        queryWrapper.eq(ApArticle::getIsDown,false);
        queryWrapper.eq(ApArticle::getIsDelete,false);

        if(dto.getChannelId() != 0){  //频道
            queryWrapper.eq(ApArticle::getChannelId,dto.getChannelId());
        }
        if(type==2){          // 加载更多： 发布时间< minTime
            queryWrapper.lt(ApArticle::getPublishTime,dto.getMinTime());
        }else{         // 第一次加载和加载更新 发布时间>maxTime
            queryWrapper.gt(ApArticle::getPublishTime,dto.getMaxTime());
        }

        queryWrapper.orderByDesc(ApArticle::getPublishTime);

        Page<ApArticle> page = new Page<>(1, size);

        Page<ApArticle> page1 = this.page(page, queryWrapper);

        return ResponseResult.okResult(page1.getRecords());
    }

    @Override
    public ResponseResult loadArticleInfo(ArticleInfoDto dto) {

        if(dto==null||dto.getArticleId()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }


        log.error("ArticleId:"+dto.getArticleId());

        Map resultMap = new HashMap();

        // 查询两张表的数据
         ApArticle apArticle = this.getById(dto.getArticleId());
         if(apArticle==null){
             return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
         }


         LambdaQueryWrapper<ApArticleContent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApArticleContent::getArticleId,dto.getArticleId());


         ApArticleContent apArticleContent = apArticleContentService.getOne(queryWrapper);
         if(apArticleContent==null){
             return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
         }

         resultMap.put("config",apArticle);
         resultMap.put("content",apArticleContent);


        return ResponseResult.okResult(resultMap);
    }

    @Override
    public void computeHotArticle() {
        // 1、查询 最近 500天的所有未下架未删除的文章
        // select * from ap_article where published_time> (当前时间-500天) and
        // 获取500天之前的日期    DateTime  joda
        Date beforeDate = DateTime.now().minusDays(500).toDate();
        LambdaQueryWrapper<ApArticle> queryWrapper = new LambdaQueryWrapper<ApArticle>();
        queryWrapper.eq(ApArticle::getIsDown,false);
        queryWrapper.eq(ApArticle::getIsDelete,false);
        queryWrapper.ge(ApArticle::getPublishTime,beforeDate);

        List<ApArticle> list = this.list(queryWrapper);


        // 缓存时需要把之前的数据删除
        Set<String> keys = redisTemplate.keys("hot_article_first_page_*");
        redisTemplate.delete(keys);


        // 2、计算分值   阅读数204    点赞数1   评论数1    收藏数1  204*1+1*3+1*5+1*8
        for (ApArticle article : list) {
            Integer score = computeScore(article);
            // 3、放到Redis中的Zset结构中  hot_article_first_page_频道ID    hot_article_first_page_0
            // 放到自己的频道中
            ApArticle cache = new ApArticle();
            cache.setId(article.getId());
            cache.setTitle(article.getTitle());
            cache.setAuthorId(article.getAuthorId());
            cache.setAuthorName(article.getAuthorName());
            cache.setImages(article.getImages());
            cache.setStaticUrl(article.getStaticUrl());
            cache.setLayout(article.getLayout());

            redisTemplate.boundZSetOps("hot_article_first_page_"+article.getChannelId()).add(JSON.toJSONString(cache),score);

            // 放到统一的推荐频道中
            redisTemplate.boundZSetOps("hot_article_first_page_0").add(JSON.toJSONString(cache),score);
        }

    }

    private Integer computeScore(ApArticle article) {
        Integer score = 0;
        if(article.getViews()!=null){
            score+=article.getViews()*1;
        }

        if(article.getLikes()!=null){
            score+=article.getLikes()*3;
        }
        if(article.getComment()!=null){
            score+=article.getComment()*5;
        }
        if(article.getCollection()!=null){
            score+=article.getCollection()*8;
        }

        return score;


    }
}
