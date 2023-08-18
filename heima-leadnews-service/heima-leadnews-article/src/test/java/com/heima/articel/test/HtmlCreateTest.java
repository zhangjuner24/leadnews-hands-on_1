package com.heima.articel.test;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.article.ArticleApp;
import com.heima.article.entity.ApArticle;
import com.heima.article.entity.ApArticleContent;
import com.heima.article.service.IApArticleContentService;
import com.heima.article.service.IApArticleService;
import com.heima.common.minio.MinIOService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = ArticleApp.class)
public class HtmlCreateTest {

    @Autowired
    private IApArticleContentService apArticleContentService;
    @Autowired
    private IApArticleService apArticleService;
    @Autowired
    private Configuration configuration;
    @Autowired
    private MinIOService minIOService;

    @Test
    public void createHtml(ApArticle apArticle) throws Exception {
        Long articleId = apArticle.getId();

        // 根据ID查询文章
        // ApArticle apArticle = apArticleService.getById(articleId);
        if(apArticle==null){
            return;
        }

        // 根据文章ID查询content
        LambdaQueryWrapper<ApArticleContent> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ApArticleContent::getArticleId,articleId);
        // 先获取数据
        ApArticleContent articleContent = apArticleContentService.getOne(queryWrapper);
        if(articleContent==null){
            return;
        }
        // 只获取内容，并且转成集合
        List<Map> mapList = JSON.parseArray(articleContent.getContent(), Map.class);
        // 获取模板
        Template template = configuration.getTemplate("article.ftl");
        // 模拟+数据


        // 构建Freemarker需要的数据
        Map dataModel = new HashMap();
        dataModel.put("content",mapList);
        StringWriter writer = new StringWriter();
        template.process(dataModel,writer);



        // 转成minio需要的inputStream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(writer.toString().getBytes());

        // 上传到MINIO
        minIOService.upload("/article/"+articleId+".html",inputStream,"text/html");

        // "http://192.168.85.143:9000/heima/article/"+articleId+".html
        // 更新表中的staticUrl
        apArticle.setStaticUrl("http://192.168.85.143:9000/heima/article/"+articleId+".html");
        apArticleService.updateById(apArticle);

    }

    @Test
    public void createAllHtml() throws Exception{
        // 所有未下架 未删除的app文章
        LambdaQueryWrapper<ApArticle> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ApArticle::getIsDelete,false);
        queryWrapper.eq(ApArticle::getIsDown,false);
        List<ApArticle> list = apArticleService.list(queryWrapper);


        for (ApArticle apArticle : list) {
            createHtml(apArticle);
        }
    }
}
