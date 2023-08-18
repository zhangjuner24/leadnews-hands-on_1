package com.heima.user.controller;

import com.heima.common.dto.ResponseResult;
import com.heima.user.entity.ApUser;
import com.heima.user.service.IApUserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * APP用户信息表 前端控制器
 * </p>
 *
 * @author syl
 * @since 2023-04-16
 */
@RestController
@RequestMapping("/api/v1/user")
@Api(tags = "APP用户信息表接口")
@CrossOrigin
public class ApUserController{

    @Autowired
    private IApUserService apUserService;

    @GetMapping("/{id}")
    public ResponseResult<ApUser> getById(@PathVariable("id") Integer id) {
        ApUser apUser = apUserService.getById(id);
        return ResponseResult.okResult(apUser);
    }

}
