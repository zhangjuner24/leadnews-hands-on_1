package com.heima.article.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.heima.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.article.service.IApArticleContentService;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * APP已发布文章内容表 前端控制器
 * </p>
 *
 * @author syl
 * @since 2023-04-19
 */
@RestController
@RequestMapping("/api/v1/article_content")
@Api(tags = "APP已发布文章内容表接口")
@CrossOrigin
public class ApArticleContentController{

    @Autowired
    private IApArticleContentService apArticleContentService;



}
