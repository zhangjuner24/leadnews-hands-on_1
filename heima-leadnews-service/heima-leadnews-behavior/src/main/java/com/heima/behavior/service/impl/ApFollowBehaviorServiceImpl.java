package com.heima.behavior.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.behavior.dto.BehaviorDto;
import com.heima.behavior.entity.ApBehaviorEntry;
import com.heima.behavior.entity.ApFollowBehavior;
import com.heima.behavior.mapper.ApFollowBehaviorMapper;
import com.heima.behavior.service.IApBehaviorEntryService;
import com.heima.behavior.service.IApFollowBehaviorService;
import com.heima.common.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * APP关注行为表 服务实现类
 * </p>
 *
 * @author syl
 * @since 2023-04-27
 */
@Service
public class ApFollowBehaviorServiceImpl extends ServiceImpl<ApFollowBehaviorMapper, ApFollowBehavior> implements IApFollowBehaviorService {

    @Autowired
    private IApBehaviorEntryService apBehaviorEntryService;

    @Override
    public ResponseResult saveFollow(BehaviorDto dto) {

       ApBehaviorEntry entry = apBehaviorEntryService.getEntryByUserId(dto.getUserId());


        ApFollowBehavior apFollowBehavior = new ApFollowBehavior();
        // apFollowBehavior.setId(0L);  mysql主键自增
        apFollowBehavior.setEntryId(entry.getId()); //实体id 从ap_behavior_entry获取
        apFollowBehavior.setFollowId(dto.getAuthorId()); // 关注的是谁
        apFollowBehavior.setOperation(dto.getOperation());// 操作类型 0  关注 点赞 不喜欢 收藏    1 取消关注 取消点赞 取消不喜欢 取消收藏
        apFollowBehavior.setCreatedTime(new Date());

        this.save(apFollowBehavior);
        return ResponseResult.okResult();
    }
}
