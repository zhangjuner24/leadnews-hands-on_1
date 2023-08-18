package com.heima.behavior.service;

import com.heima.behavior.dto.BehaviorDto;
import com.heima.behavior.entity.ApUnlikesBehavior;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.common.dto.ResponseResult;

/**
 * <p>
 * APP不喜欢行为表 服务类
 * </p>
 *
 * @author syl
 * @since 2023-04-27
 */
public interface IApUnlikesBehaviorService extends IService<ApUnlikesBehavior> {

    ResponseResult saveUnlikes(BehaviorDto dto);
}
