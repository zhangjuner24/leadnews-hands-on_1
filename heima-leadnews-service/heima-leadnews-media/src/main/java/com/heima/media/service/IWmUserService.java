package com.heima.media.service;

import com.heima.common.dto.ResponseResult;
import com.heima.media.dto.LoginDto;
import com.heima.media.entity.WmUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 自媒体用户信息表 服务类
 * </p>
 *
 * @author syl
 * @since 2023-04-16
 */
public interface IWmUserService extends IService<WmUser> {

    ResponseResult<WmUser> saveWmUser(WmUser wmUser);

    ResponseResult login(LoginDto dto);
}
