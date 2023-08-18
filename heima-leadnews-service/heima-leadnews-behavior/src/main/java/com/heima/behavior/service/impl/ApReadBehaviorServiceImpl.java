package com.heima.behavior.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.behavior.dto.BehaviorDto;
import com.heima.behavior.entity.ApBehaviorEntry;
import com.heima.behavior.entity.ApReadBehavior;
import com.heima.behavior.mapper.ApReadBehaviorMapper;
import com.heima.behavior.service.IApBehaviorEntryService;
import com.heima.behavior.service.IApReadBehaviorService;
import com.heima.common.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * APP阅读行为表 服务实现类
 * </p>
 *
 * @author syl
 * @since 2023-04-27
 */
@Service
public class ApReadBehaviorServiceImpl extends ServiceImpl<ApReadBehaviorMapper, ApReadBehavior> implements IApReadBehaviorService {

    @Autowired
    private IApBehaviorEntryService apBehaviorEntryService;

    @Override
    public ResponseResult saveRead(BehaviorDto dto) {

        ApBehaviorEntry entry = apBehaviorEntryService.getEntryByUserId(dto.getUserId());

        ApReadBehavior apReadBehavior = new ApReadBehavior();
        // apReadBehavior.setId(0L);
        apReadBehavior.setEntryId(entry.getId());
        apReadBehavior.setArticleId(dto.getArticleId());
        apReadBehavior.setCount(dto.getCount());
        apReadBehavior.setReadDuration(dto.getReadDuration());
        apReadBehavior.setPercentage(dto.getPercentage());
        apReadBehavior.setLoadDuration(dto.getLoadDuration());
        apReadBehavior.setCreatedTime(new Date());
        apReadBehavior.setUpdatedTime(new Date());

        this.save(apReadBehavior);

        return ResponseResult.okResult();
    }
}
