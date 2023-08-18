package com.heima.article.task;

import com.alibaba.fastjson.JSON;
import com.heima.article.entity.ApArticle;
import com.heima.article.service.IApArticleContentService;
import com.heima.article.service.IApArticleService;
import com.heima.common.minio.MinIOService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CreateHtmlTask {


    @Autowired
    private IApArticleContentService apArticleContentService;
    @Autowired
    private IApArticleService apArticleService;
    @Autowired
    private Configuration configuration;
    @Autowired
    private MinIOService minIOService;

    @Async("asyncExecutor")
    public void createHtml(ApArticle apArticle,String content){
        try {
            Long articleId = apArticle.getId();

            // 根据ID查询文章
            // ApArticle apArticle = apArticleService.getById(articleId);
            if(apArticle==null){
                return;
            }


            // 只获取内容，并且转成集合
            List<Map> mapList = JSON.parseArray(content, Map.class);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
