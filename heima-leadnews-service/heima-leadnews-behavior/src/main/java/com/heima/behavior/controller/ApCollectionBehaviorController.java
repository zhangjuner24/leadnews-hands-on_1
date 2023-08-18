package com.heima.behavior.controller;

import com.heima.behavior.dto.BehaviorDto;
import com.heima.behavior.service.IApCollectionBehaviorService;
import com.heima.common.dto.ResponseResult;
import com.heima.common.dto.User;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.util.UserThreadLocalUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * APP收藏信息表 前端控制器
 * </p>
 *
 * @author syl
 * @since 2023-04-27
 */
@RestController
@RequestMapping("/api/v1/collection_behavior")
@Api(tags = "APP收藏信息表接口")
@CrossOrigin
public class ApCollectionBehaviorController{

    @Autowired
    private IApCollectionBehaviorService apCollectionBehaviorService;
    @PostMapping
    @ApiOperation("保存收藏行为")
    public ResponseResult saveCollection(@RequestBody BehaviorDto dto){
        // 判断是否有登录人
        User user = UserThreadLocalUtil.get();
        if(user == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        dto.setUserId(user.getUserId()); //跟当前登录人赋值
        return apCollectionBehaviorService.saveCollection(dto);
    }


}
