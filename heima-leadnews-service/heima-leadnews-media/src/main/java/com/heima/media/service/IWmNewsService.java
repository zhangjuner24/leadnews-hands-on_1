package com.heima.media.service;

import com.heima.common.dto.ResponseResult;
import com.heima.media.dto.WmNewsDto;
import com.heima.media.dto.WmNewsPageDto;
import com.heima.media.entity.WmNews;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 自媒体图文内容信息表 服务类
 * </p>
 *
 * @author syl
 * @since 2023-04-18
 */
public interface IWmNewsService extends IService<WmNews> {

    ResponseResult listByCondition(WmNewsPageDto dto);

    ResponseResult submit(WmNewsDto dto);

    ResponseResult downOrUp(WmNewsDto dto);
}
