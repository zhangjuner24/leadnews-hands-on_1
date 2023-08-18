package com.heima.user.controller;

import com.heima.common.dto.ResponseResult;
import com.heima.user.dto.LoginDto;
import com.heima.user.service.IApUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * app用户信息表 前端控制器
 * </p>
 *
 * @author syl
 * @since 2023-04-16
 */
@RestController
@RequestMapping("/api/v1/login")
@Api(tags = "app登录")
@CrossOrigin
public class LoginController {

    @Autowired
    private IApUserService apUserService;

    @PostMapping("/login_auth")
    @ApiOperation("app登录登录")
    public ResponseResult login(@RequestBody LoginDto dto){

        return apUserService.login(dto);

    }


}
