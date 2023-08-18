package com.heima.article.listener;

import com.alibaba.fastjson.JSON;
import com.heima.article.entity.ApArticle;
import com.heima.article.service.IApArticleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UpOrDownListener {

    @Autowired
    private IApArticleService apArticleService;

    @KafkaListener(topics = "${topic.upDownTopic}")
    public void receiveMsg(ConsumerRecord<String,String> record){
        String value = record.value();
        if(StringUtils.isNotBlank(value)){
            Map map = JSON.parseObject(value, Map.class);
            // map.put("articleId",wmNews.getArticleId());
            // map.put("isDown",dto.getEnable()==0); // 要和Enable的值取反
            Long articleId = (Long) map.get("articleId");
            Boolean isDown = (Boolean) map.get("isDown");

            ApArticle apArticle = apArticleService.getById(articleId);

            if (apArticle!=null) {
                apArticle.setIsDown(isDown);
                apArticleService.updateById(apArticle);
            }
        }
    }
}
