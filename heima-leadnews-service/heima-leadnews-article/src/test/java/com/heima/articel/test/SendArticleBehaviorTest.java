package com.heima.articel.test;

import com.alibaba.fastjson.JSON;
import com.heima.article.ArticleApp;
import com.heima.article.dto.UpdateArticleMessage;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest(classes = ArticleApp.class)
public class SendArticleBehaviorTest {

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Test
    public void testSend(){

        for (int i = 0; i < 500; i++) {
            UpdateArticleMessage articleMessage = new UpdateArticleMessage();
            articleMessage.setType(RandomUtils.nextInt(0,4)); // 0 阅读 1 点赞 2 评论 3 收藏
            articleMessage.setArticleId(1650669064680382465L);
            articleMessage.setAdd(1);
            kafkaTemplate.send("hot_article_score_topic", JSON.toJSONString(articleMessage));
        }
        for (int i = 0; i < 500; i++) {
            UpdateArticleMessage articleMessage = new UpdateArticleMessage();
            articleMessage.setType(RandomUtils.nextInt(0,4)); // 0 阅读 1 点赞 2 评论 3 收藏
            articleMessage.setArticleId(1650667956524294146L);
            articleMessage.setAdd(1);
            kafkaTemplate.send("hot_article_score_topic", JSON.toJSONString(articleMessage));
        }
    }

}
