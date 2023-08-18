package com.heima.behavior.service;

import com.heima.behavior.entity.ApBehaviorEntry;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * APP行为实体表,一个行为实体可能是用户或者设备，或者其它 服务类
 * </p>
 *
 * @author syl
 * @since 2023-04-27
 */
public interface IApBehaviorEntryService extends IService<ApBehaviorEntry> {

    ApBehaviorEntry getEntryByUserId(Integer userId);
}
