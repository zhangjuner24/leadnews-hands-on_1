package com.heima.article.controller;

import com.heima.article.dto.ArticleDto;
import com.heima.article.dto.ArticleHomeDto;
import com.heima.article.dto.ArticleInfoDto;
import com.heima.article.service.IApArticleService;
import com.heima.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 文章信息表，存储已发布的文章 前端控制器
 * </p>
 *
 * @author syl
 * @since 2023-04-19
 */
@RestController
@RequestMapping("/api/v1/article")
@Api(tags = "文章信息表，存储已发布的文章接口")
@CrossOrigin
public class ApArticleController{


    @Autowired
    private IApArticleService apArticleService;


    @PostMapping
    @ApiOperation("新增App文章")
    public ResponseResult<Long> saveApArticle(@RequestBody ArticleDto dto){
        return apArticleService.saveApArticle(dto);
    }

    @PostMapping("/load")
    @ApiOperation("第一次加载文章列表")
    public ResponseResult load(@RequestBody ArticleHomeDto dto){
        return apArticleService.load(dto,1);  // 1:第一次加载   2：加载更多  3：加载更新
    }

    @PostMapping("/loadmore")
    @ApiOperation("加载更多文章列表")
    public ResponseResult loadmore(@RequestBody ArticleHomeDto dto){
        return apArticleService.load(dto,2);
    }

    @PostMapping("/loadnew")
    @ApiOperation("加载更新文章列表")
    public ResponseResult loadnew(@RequestBody ArticleHomeDto dto){
        return apArticleService.load(dto,3);
    }

     @PostMapping("/load_article_info")
    @ApiOperation("根据id查询文章详情")
    public ResponseResult loadArticleInfo(@RequestBody ArticleInfoDto dto){
        return apArticleService.loadArticleInfo(dto);
    }


}
