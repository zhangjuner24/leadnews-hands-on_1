package com.heima.behavior.service;

import com.heima.behavior.dto.BehaviorDto;
import com.heima.behavior.entity.ApLikesBehavior;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.common.dto.ResponseResult;

/**
 * <p>
 * APP点赞行为表 服务类
 * </p>
 *
 * @author syl
 * @since 2023-04-27
 */
public interface IApLikesBehaviorService extends IService<ApLikesBehavior> {

    ResponseResult saveLikes(BehaviorDto dto);
}
