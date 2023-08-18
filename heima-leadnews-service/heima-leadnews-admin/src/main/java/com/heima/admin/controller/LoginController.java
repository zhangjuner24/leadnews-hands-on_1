package com.heima.admin.controller;

import com.heima.admin.dto.LoginDto;
import com.heima.admin.service.IAdUserService;
import com.heima.common.dto.ResponseResult;
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
@Api(tags = "管理员登录")
@CrossOrigin
public class LoginController {

    @Autowired
    private IAdUserService adUserService;

    @PostMapping("/in")
    @ApiOperation("管理员登录")
    public ResponseResult login(@RequestBody LoginDto dto){

        return adUserService.login(dto);

    }


}
