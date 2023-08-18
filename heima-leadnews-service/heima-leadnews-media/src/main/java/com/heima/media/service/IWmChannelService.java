package com.heima.media.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.common.dto.ResponseResult;
import com.heima.media.dto.ChannelDto;
import com.heima.media.entity.WmChannel;

public interface IWmChannelService extends IService<WmChannel> {
    ResponseResult listByName(ChannelDto dto);

    ResponseResult saveWmChannel(WmChannel wmChannel);
}
