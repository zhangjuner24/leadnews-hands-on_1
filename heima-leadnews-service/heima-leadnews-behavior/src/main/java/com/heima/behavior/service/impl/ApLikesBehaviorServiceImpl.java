package com.heima.behavior.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.behavior.dto.BehaviorDto;
import com.heima.behavior.entity.ApBehaviorEntry;
import com.heima.behavior.entity.ApLikesBehavior;
import com.heima.behavior.mapper.ApLikesBehaviorMapper;
import com.heima.behavior.service.IApBehaviorEntryService;
import com.heima.behavior.service.IApLikesBehaviorService;
import com.heima.common.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * APP点赞行为表 服务实现类
 * </p>
 *
 * @author syl
 * @since 2023-04-27
 */
@Service
public class ApLikesBehaviorServiceImpl extends ServiceImpl<ApLikesBehaviorMapper, ApLikesBehavior> implements IApLikesBehaviorService {
    @Autowired
    private IApBehaviorEntryService apBehaviorEntryService;
    @Override
    public ResponseResult saveLikes(BehaviorDto dto) {

        ApBehaviorEntry entry = apBehaviorEntryService.getEntryByUserId(dto.getUserId());
        ApLikesBehavior apLikesBehavior = new ApLikesBehavior();
        // apLikesBehavior.setId(0L);
        apLikesBehavior.setEntryId(entry.getId());
        apLikesBehavior.setArticleId(dto.getArticleId());
        apLikesBehavior.setType(0);  //0文章            1动态
        apLikesBehavior.setOperation(dto.getOperation());
        apLikesBehavior.setCreatedTime(new Date());

        this.save(apLikesBehavior);
        return ResponseResult.okResult();
    }
}
