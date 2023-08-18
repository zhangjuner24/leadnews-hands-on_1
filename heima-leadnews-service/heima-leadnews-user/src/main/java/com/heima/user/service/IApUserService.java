package com.heima.user.service;

import com.heima.common.dto.ResponseResult;
import com.heima.user.dto.LoginDto;
import com.heima.user.entity.ApUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * APP用户信息表 服务类
 * </p>
 *
 * @author syl
 * @since 2023-04-16
 */
public interface IApUserService extends IService<ApUser> {

    ResponseResult login(LoginDto dto);

}
