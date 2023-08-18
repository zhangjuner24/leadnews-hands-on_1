package com.heima.admin.service;

import com.heima.admin.dto.LoginDto;
import com.heima.admin.entity.AdUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.common.dto.ResponseResult;

/**
 * <p>
 * 管理员用户信息表 服务类
 * </p>
 *
 * @author syl
 * @since 2023-04-16
 */
public interface IAdUserService extends IService<AdUser> {

    ResponseResult login(LoginDto dto);
}
