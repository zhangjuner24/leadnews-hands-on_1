package com.heima.article.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.heima.article.entity.ApAuthor;
import com.heima.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.article.service.IApAuthorService;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * APP文章作者信息表 前端控制器
 * </p>
 *
 * @author syl
 * @since 2023-04-16
 */
@RestController
@RequestMapping("/api/v1/author")
@Api(tags = "APP文章作者信息表接口")
@CrossOrigin
public class ApAuthorController{

    @Autowired
    private IApAuthorService apAuthorService;

    @PostMapping
    public ResponseResult<ApAuthor> saveApAuthor(@RequestBody ApAuthor apAuthor){



        return apAuthorService.saveApAuthor(apAuthor);
    }

}
