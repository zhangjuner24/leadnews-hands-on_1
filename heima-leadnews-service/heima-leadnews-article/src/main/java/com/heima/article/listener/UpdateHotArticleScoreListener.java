package com.heima.article.listener;


import com.alibaba.fastjson.JSON;
import com.heima.article.dto.ArticleStreamMessage;
import com.heima.article.service.IApArticleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

// @Component
public class UpdateHotArticleScoreListener {

    @Autowired
    private IApArticleService apArticleService;

    @KafkaListener(topics = "hot_article_score_result_topic")
    public void updateHotArticleScore(ConsumerRecord<String,String> record){
        String value = record.value();
        if(StringUtils.isNotBlank(value)){
  // {"articleId":1650669064680382465,views:2,likes:2,comment:0,collect:1} ArticleStreamMessage

            ArticleStreamMessage articleStreamMessage = JSON.parseObject(value, ArticleStreamMessage.class);

            // apArticleService.updateHotArticleScore(articleStreamMessage);

        }

    }
}
