package com.heima.behavior.service;

import com.heima.behavior.dto.BehaviorDto;
import com.heima.behavior.entity.ApCollectionBehavior;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.common.dto.ResponseResult;

/**
 * <p>
 * APP收藏信息表 服务类
 * </p>
 *
 * @author syl
 * @since 2023-04-27
 */
public interface IApCollectionBehaviorService extends IService<ApCollectionBehavior> {

    ResponseResult saveCollection(BehaviorDto dto);
}
