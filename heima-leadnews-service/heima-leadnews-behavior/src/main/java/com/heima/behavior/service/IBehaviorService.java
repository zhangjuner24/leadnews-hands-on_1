package com.heima.behavior.service;

import com.heima.behavior.dto.BehaviorDto;
import com.heima.common.dto.ResponseResult;

public interface IBehaviorService {
    ResponseResult loadBehavior(BehaviorDto dto);
}

