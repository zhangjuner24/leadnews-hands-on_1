package com.heima.article.streams;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

public interface HotArticleProcessor {
    @Input("hot_article_score_topic")
    KStream<String, String> input();

    /**
     * Output binding.
     * @return {@link Output} binding for {@link KStream} type.
     */
    @Output("hot_article_score_result_topic")
    KStream<String,String> output();
}
