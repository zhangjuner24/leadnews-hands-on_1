package com.heima.behavior.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.behavior.dto.BehaviorDto;
import com.heima.behavior.entity.ApBehaviorEntry;
import com.heima.behavior.entity.ApUnlikesBehavior;
import com.heima.behavior.mapper.ApUnlikesBehaviorMapper;
import com.heima.behavior.service.IApBehaviorEntryService;
import com.heima.behavior.service.IApUnlikesBehaviorService;
import com.heima.common.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * APP不喜欢行为表 服务实现类
 * </p>
 *
 * @author syl
 * @since 2023-04-27
 */
@Service
public class ApUnlikesBehaviorServiceImpl extends ServiceImpl<ApUnlikesBehaviorMapper, ApUnlikesBehavior> implements IApUnlikesBehaviorService {
    @Autowired
    private IApBehaviorEntryService apBehaviorEntryService;

    @Override
    public ResponseResult saveUnlikes(BehaviorDto dto) {

        ApBehaviorEntry entry = apBehaviorEntryService.getEntryByUserId(dto.getUserId());

        ApUnlikesBehavior unlikesBehavior = new ApUnlikesBehavior();
        // unlikesBehavior.setId(0L);
        unlikesBehavior.setEntryId(entry.getId());
        unlikesBehavior.setArticleId(dto.getArticleId());
        unlikesBehavior.setOperation(dto.getOperation());
        unlikesBehavior.setCreatedTime(new Date());
        this.save(unlikesBehavior);

        return ResponseResult.okResult();
    }
}
