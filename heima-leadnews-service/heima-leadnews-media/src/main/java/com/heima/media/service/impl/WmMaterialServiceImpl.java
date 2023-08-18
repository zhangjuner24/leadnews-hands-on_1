package com.heima.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.dto.PageResponseResult;
import com.heima.common.dto.ResponseResult;
import com.heima.common.dto.User;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.minio.MinIOService;
import com.heima.common.util.UserThreadLocalUtil;
import com.heima.media.dto.WmMaterialDto;
import com.heima.media.entity.WmMaterial;
import com.heima.media.mapper.WmMaterialMapper;
import com.heima.media.service.IWmMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * <p>
 * 自媒体图文素材信息表 服务实现类
 * </p>
 *
 * @author syl
 * @since 2023-04-18
 */
@Service
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements IWmMaterialService {

    @Autowired
    private MinIOService minIOService;

    @Override
    public ResponseResult<WmMaterial> saveWmMaterial(MultipartFile file) {

        User user = UserThreadLocalUtil.get();
        if(user==null){ //必须登录后才能操作此方法
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        // 上传URL
        String url = minIOService.upload(file);

        WmMaterial wmMaterial = new WmMaterial();
        // wmMaterial.setId(0); //自增
        wmMaterial.setUserId(user.getUserId()); //当前登录人
        wmMaterial.setUrl(url); // minio的URL
        wmMaterial.setType(0);  //素材类型            0 图片            1 视频
        wmMaterial.setIsCollection(false);
        wmMaterial.setCreatedTime(new Date());
        this.save(wmMaterial);
        return ResponseResult.okResult(wmMaterial);
    }

    @Override
    public ResponseResult<WmMaterial> listByStatus(WmMaterialDto dto) {
        User user = UserThreadLocalUtil.get();
        if(user==null){ //必须登录后才能操作此方法
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }



        LambdaQueryWrapper<WmMaterial> queryWrapper = new LambdaQueryWrapper<>();
        if(dto.getIsCollection()!=null){
          queryWrapper.eq(WmMaterial::getIsCollection,dto.getIsCollection());
        }
        // 隐藏的条件：登录人
        queryWrapper.eq(WmMaterial::getUserId,user.getUserId());
        // 排序： 是否收藏\时间
        queryWrapper.orderByDesc(WmMaterial::getCreatedTime);


        Page<WmMaterial> page = new Page<>(dto.getPage(),dto.getSize());
        Page<WmMaterial> page1 = this.page(page, queryWrapper);
        return new PageResponseResult(dto.getPage(),dto.getSize(),page1.getTotal(),page1.getRecords());
    }
}
