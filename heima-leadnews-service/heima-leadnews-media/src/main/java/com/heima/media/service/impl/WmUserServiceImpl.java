package com.heima.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.common.dto.ResponseResult;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.util.AppJwtUtil;
import com.heima.media.dto.LoginDto;
import com.heima.media.entity.WmUser;
import com.heima.media.mapper.WmUserMapper;
import com.heima.media.service.IWmUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 自媒体用户信息表 服务实现类
 * </p>
 *
 * @author syl
 * @since 2023-04-16
 */
@Service
public class WmUserServiceImpl extends ServiceImpl<WmUserMapper, WmUser> implements IWmUserService {

    @Override
    public ResponseResult<WmUser> saveWmUser(WmUser wmUser) {
//        判断这个app用户是否已经成为自媒体人
        LambdaQueryWrapper<WmUser> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(WmUser::getApUserId,wmUser.getApUserId());
        WmUser one = this.getOne(queryWrapper);
        if(one!=null){ //如果成为自媒体人直接返回
            return ResponseResult.okResult(one);
        }else{  //如果没有成为自媒体人 ，新增
            wmUser.setCreatedTime(new Date());
            this.save(wmUser);
            return ResponseResult.okResult(wmUser);
        }
    }

    @Override
    public ResponseResult login(LoginDto dto) {
        if (StringUtils.isBlank(dto.getName()) || StringUtils.isBlank(dto.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NAME_OR_PASSWORD_ISNULL);
        }
//        现根据用户名查询
        LambdaQueryWrapper<WmUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WmUser::getName,dto.getName());
        WmUser one = this.getOne(queryWrapper);
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

//        构建返回的数据  {user:WmUser对象，token:jwt生成的字符串}
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
