package com.heima.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.dto.PageResponseResult;
import com.heima.common.dto.ResponseResult;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.media.dto.ChannelDto;
import com.heima.media.entity.WmChannel;
import com.heima.media.mapper.WmChannelMapper;
import com.heima.media.service.IWmChannelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel> implements IWmChannelService {
    @Override
    public ResponseResult listByName(ChannelDto dto) {

//        使用MP查询
        LambdaQueryWrapper<WmChannel> queryWrapper = new LambdaQueryWrapper();
//        对name做非空判断
        if (StringUtils.isNotBlank(dto.getName())) {
//        根据name模糊查询
            queryWrapper.like(WmChannel::getName, dto.getName());
        }
//        分页
        IPage<WmChannel> iPage = new Page<>(dto.getPage(), dto.getSize());
//        实现分页查询
        IPage<WmChannel> page = this.page(iPage, queryWrapper);
//        Long currentPage 当前页码, Integer size 每页显示条数, Long total 总条数, T data当前页数据
        return new PageResponseResult(dto.getPage(), dto.getSize(), page.getTotal(), page.getRecords());
    }

    @Override
    public ResponseResult saveWmChannel(WmChannel wmChannel) {

//        先判断是否为空
        if (wmChannel == null || StringUtils.isBlank(wmChannel.getName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
//        需要判断这个频道是否存在：先查询、判断是否存在
        LambdaQueryWrapper<WmChannel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WmChannel::getName, wmChannel.getName()); // 构建条件
        WmChannel one = this.getOne(queryWrapper);
        if (one != null) {
//            已存在
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST);
        }
//        实现新增的方法
        wmChannel.setCreatedTime(new Date());
        this.save(wmChannel);

        return ResponseResult.okResult();
    }
}
