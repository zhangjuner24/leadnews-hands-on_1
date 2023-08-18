package com.heima.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.admin.dto.LoginDto;
import com.heima.admin.entity.AdUser;
import com.heima.admin.mapper.AdUserMapper;
import com.heima.admin.service.IAdUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.dto.ResponseResult;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.util.AppJwtUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 管理员用户信息表 服务实现类
 * </p>
 *
 * @author syl
 * @since 2023-04-16
 */
@Service
public class AdUserServiceImpl extends ServiceImpl<AdUserMapper, AdUser> implements IAdUserService {

    @Override
    public ResponseResult login(LoginDto dto) {
        if (StringUtils.isBlank(dto.getName()) || StringUtils.isBlank(dto.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NAME_OR_PASSWORD_ISNULL);
        }
//        现根据用户名查询
        LambdaQueryWrapper<AdUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AdUser::getName,dto.getName());
        AdUser one = this.getOne(queryWrapper);
        if(one==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.ADUSER_NOT_EXIST);
        }
//        比较密码
//        密码+盐------ 再加密-----和数据库中的密码比较
        String password_page = DigestUtils.md5Hex(dto.getPassword() + one.getSalt());
        String password_db = one.getPassword();
        if (!StringUtils.equals(password_page,password_db)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }

//        构建返回的数据  {user:AdUser对象，token:jwt生成的字符串}
        Map resultMap = new HashMap<>();
//        去除敏感信息
        one.setPassword("");
        one.setSalt("");
        one.setPhone("");
        resultMap.put("user",one);

        Map<String, Object> claimMaps = new HashMap<>();
        claimMaps.put("userId",one.getId());
        claimMaps.put("name",one.getName());
        String token = AppJwtUtil.getToken(claimMaps);
        resultMap.put("token",token);

        return ResponseResult.okResult(resultMap);
    }
}
