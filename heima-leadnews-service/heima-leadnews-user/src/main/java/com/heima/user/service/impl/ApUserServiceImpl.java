package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.common.dto.ResponseResult;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.util.AppJwtUtil;
import com.heima.user.dto.LoginDto;
import com.heima.user.entity.ApUser;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.service.IApUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * APP用户信息表 服务实现类
 * </p>
 *
 * @author syl
 * @since 2023-04-16
 */
@Service
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements IApUserService {

    @Override
    public ResponseResult login(LoginDto dto) {

        if(StringUtils.isNotBlank(dto.getPhone())&&StringUtils.isNotBlank(dto.getPassword())){
            // 实现登录
            LambdaQueryWrapper<ApUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ApUser::getPhone, dto.getPhone());
            ApUser one = this.getOne(queryWrapper);
            if(one==null){
                ResponseResult.errorResult(AppHttpCodeEnum.ADUSER_NOT_EXIST);
            }
            // 比对密码
            String password_page = DigestUtils.md5Hex(dto.getPassword() + one.getSalt());
            if (!StringUtils.equals(one.getPassword(), password_page)) {
                ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
            }
            //        构建返回的数据  {user:WmUser对象，token:jwt生成的字符串}
            Map resultMap = new HashMap<>();
//        去除敏感信息
            one.setPassword("");
            one.setSalt("");
            resultMap.put("user",one);

            Map<String, Object> claimMaps = new HashMap<>();
            claimMaps.put("userId",one.getId());
            claimMaps.put("name",one.getName());
            String token = AppJwtUtil.getToken(claimMaps);
            resultMap.put("token",token);

            return ResponseResult.okResult(resultMap);

        }
        // 判断手机号和密码是否为空 如果为空就是不登录先看看（使用0作为用户id）
        //        构建返回的数据  {user:WmUser对象，token:jwt生成的字符串}
        Map resultMap = new HashMap<>();
//        去除敏感信息
        ApUser one = new ApUser();
        one.setId(0);
        one.setName(dto.getEquipmentId());

        resultMap.put("user",one);

        Map<String, Object> claimMaps = new HashMap<>();
        claimMaps.put("userId",one.getId());
        claimMaps.put("name",one.getName());
        String token = AppJwtUtil.getToken(claimMaps);
        resultMap.put("token",token);

        return ResponseResult.okResult(resultMap);

    }
}
