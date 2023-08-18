package com.heima.article.dto;

import com.heima.article.entity.ApArticle;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleDto extends ApArticle {
    /**
     * 文章内容
     */
    private String content;
}
