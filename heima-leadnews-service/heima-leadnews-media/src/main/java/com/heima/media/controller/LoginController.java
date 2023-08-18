package com.heima.media.controller;

import com.heima.common.dto.ResponseResult;
import com.heima.media.dto.LoginDto;
import com.heima.media.service.IWmUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 管理员用户信息表 前端控制器
 * </p>
 *
 * @author syl
 * @since 2023-04-16
 */
@RestController
@RequestMapping("/login")
@Api(tags = "自媒体人登录")
@CrossOrigin
public class LoginController {

    @Autowired
    private IWmUserService wmUserService;

    @PostMapping("/in")
    @ApiOperation("自媒体人登录")
    public ResponseResult login(@RequestBody LoginDto dto){

        return wmUserService.login(dto);

    }


}
