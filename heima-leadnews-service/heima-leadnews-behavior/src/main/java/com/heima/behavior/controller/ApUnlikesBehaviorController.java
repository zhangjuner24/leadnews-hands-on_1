package com.heima.behavior.controller;

import com.heima.behavior.dto.BehaviorDto;
import com.heima.behavior.service.IApUnlikesBehaviorService;
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
 * APP不喜欢行为表 前端控制器
 * </p>
 *
 * @author syl
 * @since 2023-04-27
 */
@RestController
@RequestMapping("/api/v1/unlikes_behavior")
@Api(tags = "APP不喜欢行为表接口")
@CrossOrigin
public class ApUnlikesBehaviorController{

    @Autowired
    private IApUnlikesBehaviorService apUnlikesBehaviorService;

    @PostMapping
    @ApiOperation("保存不喜欢行为")
    public ResponseResult saveUnlikes(@RequestBody BehaviorDto dto){
        // 判断是否有登录人
        User user = UserThreadLocalUtil.get();
        if(user == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        dto.setUserId(user.getUserId()); //跟当前登录人赋值
        return apUnlikesBehaviorService.saveUnlikes(dto);
    }

}
