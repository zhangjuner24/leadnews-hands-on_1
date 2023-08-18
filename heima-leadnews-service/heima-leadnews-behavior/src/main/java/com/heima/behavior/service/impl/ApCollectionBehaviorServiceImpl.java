package com.heima.behavior.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.behavior.config.ToRedis;
import com.heima.behavior.dto.BehaviorDto;
import com.heima.behavior.entity.ApBehaviorEntry;
import com.heima.behavior.entity.ApCollectionBehavior;
import com.heima.behavior.mapper.ApCollectionBehaviorMapper;
import com.heima.behavior.service.IApBehaviorEntryService;
import com.heima.behavior.service.IApCollectionBehaviorService;
import com.heima.common.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * APP收藏信息表 服务实现类
 * </p>
 *
 * @author syl
 * @since 2023-04-27
 */
@Service
public class ApCollectionBehaviorServiceImpl extends ServiceImpl<ApCollectionBehaviorMapper, ApCollectionBehavior> implements IApCollectionBehaviorService {
    @Autowired
    private IApBehaviorEntryService apBehaviorEntryService;

    @Override
    @ToRedis
    public ResponseResult saveCollection(BehaviorDto dto) {
        ApBehaviorEntry entry = apBehaviorEntryService.getEntryByUserId(dto.getUserId());

        ApCollectionBehavior apCollectionBehavior = new ApCollectionBehavior();
        // apCollectionBehavior.setId(0L);
        apCollectionBehavior.setEntryId(entry.getId());
        apCollectionBehavior.setOperation(dto.getOperation());
        apCollectionBehavior.setArticleId(dto.getArticleId());
        apCollectionBehavior.setCollectionTime(new Date());

        this.save(apCollectionBehavior);
        return ResponseResult.okResult();
    }
}
