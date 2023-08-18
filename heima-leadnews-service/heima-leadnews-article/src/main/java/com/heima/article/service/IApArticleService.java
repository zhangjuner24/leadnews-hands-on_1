package com.heima.article.service;

import com.heima.article.dto.ArticleDto;
import com.heima.article.dto.ArticleHomeDto;
import com.heima.article.dto.ArticleInfoDto;
import com.heima.article.dto.ArticleStreamMessage;
import com.heima.article.entity.ApArticle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.common.dto.ResponseResult;

/**
 * <p>
 * 文章信息表，存储已发布的文章 服务类
 * </p>
 *
 * @author syl
 * @since 2023-04-19
 */
public interface IApArticleService extends IService<ApArticle> {

    ResponseResult<Long> saveApArticle(ArticleDto dto);

    ResponseResult load(ArticleHomeDto dto, int i);

    ResponseResult loadArticleInfo(ArticleInfoDto dto);

    void computeHotArticle();
}
