package com.heima.media.controller;

import com.heima.common.dto.ResponseResult;
import com.heima.media.dto.ChannelDto;
import com.heima.media.entity.WmChannel;
import com.heima.media.service.IWmChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/channel")
@Api(tags="频道管理")
public class WmChannelController {

    @Autowired
    private IWmChannelService channelService;

    @PostMapping("/list")
    @ApiOperation("根据名称分页查询")
    public ResponseResult listByName(@RequestBody ChannelDto dto){  //@RequestBody用json接收
        return channelService.listByName(dto);
    }
    @PostMapping()
    @ApiOperation("新增频道")
    public ResponseResult saveWmChannel(@RequestBody WmChannel wmChannel){  //@RequestBody用json接收
        return channelService.saveWmChannel(wmChannel);
    }

    @GetMapping("/channels")
    @ApiOperation("获取所有频道")
    public ResponseResult channels(){
        List<WmChannel> list = channelService.list();
        return  ResponseResult.okResult(list);
    }

}
