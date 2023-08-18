package com.heima.media.service;

import com.heima.common.dto.ResponseResult;
import com.heima.media.dto.WmMaterialDto;
import com.heima.media.entity.WmMaterial;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 自媒体图文素材信息表 服务类
 * </p>
 *
 * @author syl
 * @since 2023-04-18
 */
public interface IWmMaterialService extends IService<WmMaterial> {

    ResponseResult<WmMaterial> saveWmMaterial(MultipartFile file);

    ResponseResult<WmMaterial> listByStatus(WmMaterialDto dto);
}
