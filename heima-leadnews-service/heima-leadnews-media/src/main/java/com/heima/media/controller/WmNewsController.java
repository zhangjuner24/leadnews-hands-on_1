package com.heima.media.controller;

import com.heima.common.dto.ResponseResult;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.media.dto.WmNewsDto;
import com.heima.media.dto.WmNewsPageDto;
import com.heima.media.entity.WmNews;
import com.heima.media.service.IWmNewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 自媒体图文内容信息表 前端控制器
 * </p>
 *
 * @author syl
 * @since 2023-04-18
 */
@RestController
@RequestMapping("/api/v1/news")
@Api(tags = "自媒体图文内容信息表接口")
@CrossOrigin
public class WmNewsController{

    @Autowired
    private IWmNewsService wmNewsService;

    @PostMapping("/list")
    @ApiOperation("分页带条件查询自媒体文章")
    public ResponseResult listByCondition(@RequestBody WmNewsPageDto dto){
        return wmNewsService.listByCondition(dto);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询自媒体文章")
    public ResponseResult getById(@PathVariable Integer id){
        WmNews wmNews = wmNewsService.getById(id);
        if(wmNews==null){
            ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        return ResponseResult.okResult(wmNews);
    }

    @PostMapping("/submit")
    @ApiOperation("发布自媒体文章")
    public ResponseResult submit(@RequestBody WmNewsDto dto){
        return wmNewsService.submit(dto);
    }

    @PutMapping("/down_or_up")
    @ApiOperation("自媒体文章的上下架")
    public ResponseResult downOrUp(@RequestBody WmNewsDto dto){
        return wmNewsService.downOrUp(dto);
    }

}
