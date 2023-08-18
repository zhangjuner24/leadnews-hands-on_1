package com.heima.behavior.controller;

import com.heima.behavior.dto.BehaviorDto;
import com.heima.behavior.service.IBehaviorService;
import com.heima.common.dto.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/behavior")
public class BehaviorController {
    @Autowired
    private IBehaviorService behaviorService;

    @PostMapping("/load_article_behavior")
    @ApiOperation("加载用户行为数据")
    public ResponseResult loadBehavior(@RequestBody BehaviorDto dto){

        return behaviorService.loadBehavior(dto);
    }
}
