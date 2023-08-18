package com.heima.article.streams;


import com.alibaba.fastjson.JSON;
import com.heima.article.dto.ArticleStreamMessage;
import com.heima.article.dto.UpdateArticleMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;

import java.time.Duration;

@EnableBinding(HotArticleProcessor.class)
public class HotArticleProcessorListener {

    @StreamListener("hot_article_score_topic")
    @SendTo("hot_article_score_result_topic")
    public KStream<String, String> process(KStream<String, String> input) {
        // {"add":1,"articleId":1650669064680382465,"type":3}
        // {"add":1,"articleId":1650669064680382465,"type":1}
        // {"add":1,"articleId":1650669064680382465,"type":0}
        // {"add":1,"articleId":1650667956524294146,"type":0}
        // {"add":1,"articleId":1650667956524294146,"type":3}
        // {"add":1,"articleId":1650669064680382465,"type":0}
        // {"add":1,"articleId":1650667956524294146,"type":2}
        // {"add":1,"articleId":1650667956524294146,"type":3}
        // {"add":1,"articleId":1650669064680382465,"type":1}
        // {"add":1,"articleId":1650667956524294146,"type":0}

        KStream<String, String> stringKStream = input.map(new KeyValueMapper<String, String, KeyValue<String, String>>() {
            @Override
            public KeyValue<String, String> apply(String key, String value) {
                // value的值是 {"add":1,"articleId":1650667956524294146,"type":0} UpdateArticleMessage
                UpdateArticleMessage updateArticleMessage = JSON.parseObject(value, UpdateArticleMessage.class);
                return new KeyValue<>(updateArticleMessage.getArticleId().toString(), value);
            }
        });
      /*  // 第一步：设置key stringKStream的数据是
        1650669064680382465：{"add":1,"articleId":1650669064680382465,"type":3}
        1650669064680382465：{"add":1,"articleId":1650669064680382465,"type":1}
        1650669064680382465：{"add":1,"articleId":1650669064680382465,"type":0}
        1650667956524294146：{"add":1,"articleId":1650667956524294146,"type":0}
        1650667956524294146：{"add":1,"articleId":1650667956524294146,"type":3}
        1650669064680382465：{"add":1,"articleId":1650669064680382465,"type":0}
        1650667956524294146：{"add":1,"articleId":1650667956524294146,"type":2}
        1650667956524294146：{"add":1,"articleId":1650667956524294146,"type":3}
        1650669064680382465：{"add":1,"articleId":1650669064680382465,"type":1}
        1650667956524294146：{"add":1,"articleId":1650667956524294146,"type":0}*/

        KGroupedStream<String, String> groupedStream = stringKStream.groupByKey();
       /* 第二步：分组
        1650669064680382465：{"add":1,"articleId":1650669064680382465,"type":3}
        1650669064680382465：{"add":1,"articleId":1650669064680382465,"type":1}
        1650669064680382465：{"add":1,"articleId":1650669064680382465,"type":0}
        1650669064680382465：{"add":1,"articleId":1650669064680382465,"type":1}
        1650669064680382465：{"add":1,"articleId":1650669064680382465,"type":0}

        1650667956524294146：{"add":1,"articleId":1650667956524294146,"type":2}
        1650667956524294146：{"add":1,"articleId":1650667956524294146,"type":3}
        1650667956524294146：{"add":1,"articleId":1650667956524294146,"type":0}
        1650667956524294146：{"add":1,"articleId":1650667956524294146,"type":0}
        1650667956524294146：{"add":1,"articleId":1650667956524294146,"type":3}*/

        // 设置时间窗口
        TimeWindowedKStream<String, String> windowedKStream = groupedStream.windowedBy(TimeWindows.of(Duration.ofSeconds(60)));

        // 初始化器，再计算之前就是空
        Initializer<String> initializer = new Initializer<String>() {
            @Override
            public String apply() {
                return null;
            }
        };

        Aggregator<String, String, String> aggregator = new Aggregator<String, String, String>() {
            @Override
            public String apply(String key, String value, String aggregate) {
                // key:  1650667956524294146   value:{"add":1,"articleId":1650667956524294146,"type":3}
                // aggregate就是聚合后的结果{"articleId":1650669064680382465,view:2,like:2,comment:0,collect:1} ArticleStreamMessage
                ArticleStreamMessage articleStreamMessage = null;
                if(StringUtils.isBlank(aggregate)){
                    articleStreamMessage = new ArticleStreamMessage();
                    articleStreamMessage.setArticleId(Long.parseLong(key));
                }else{
                    articleStreamMessage = JSON.parseObject(aggregate,ArticleStreamMessage.class);
                }

                UpdateArticleMessage updateArticleMessage = JSON.parseObject(value, UpdateArticleMessage.class);
                Integer type = updateArticleMessage.getType();

                switch (type){
                    case 0:
                        articleStreamMessage.setView(articleStreamMessage.getView()+updateArticleMessage.getAdd());
                        break;
                    case 1:
                        articleStreamMessage.setLike(articleStreamMessage.getLike()+updateArticleMessage.getAdd());
                        break;
                    case 2:
                        articleStreamMessage.setComment(articleStreamMessage.getComment()+updateArticleMessage.getAdd());
                        break;
                    case 3:
                        articleStreamMessage.setCollect(articleStreamMessage.getCollect()+updateArticleMessage.getAdd());
                        break;
                    default:{

                    }
                }
                // articleStreamMessage:{"articleId":1650669064680382465,view:0,like:0,comment:0,collect:1}
                return JSON.toJSONString(articleStreamMessage); //{"articleId":1650669064680382465,view:2,like:2,comment:0,collect:1} ArticleStreamMessage
            }
        };
        // 第三步：聚合统计 每个行为产生的个数
        // {"articleId":1650669064680382465,view:2,like:2,comment:0,collect:1} ArticleStreamMessage
        // {"articleId":1650667956524294146,view:2,like:0,comment:1,collect:2} ArticleStreamMessage


        KTable<Windowed<String>, String> kTable = windowedKStream.aggregate(initializer, aggregator);

        KStream<String, String> kStream = kTable.toStream().map(new KeyValueMapper<Windowed<String>, String, KeyValue<String, String>>() {
            @Override
            public KeyValue<String, String> apply(Windowed<String> key, String value) {
                return new KeyValue<>(key.key(), value);
            }
        });

        return kStream;

    }


}
