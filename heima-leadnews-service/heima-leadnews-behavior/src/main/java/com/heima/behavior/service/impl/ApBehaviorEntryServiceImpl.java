package com.heima.behavior.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.behavior.entity.ApBehaviorEntry;
import com.heima.behavior.mapper.ApBehaviorEntryMapper;
import com.heima.behavior.service.IApBehaviorEntryService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * APP行为实体表,一个行为实体可能是用户或者设备，或者其它 服务实现类
 * </p>
 *
 * @author syl
 * @since 2023-04-27
 */
@Service
public class ApBehaviorEntryServiceImpl extends ServiceImpl<ApBehaviorEntryMapper, ApBehaviorEntry> implements IApBehaviorEntryService {

    @Override
    public ApBehaviorEntry getEntryByUserId(Integer userId) {
        // select * from ap_behavior_entry where entry_id=?
        LambdaQueryWrapper<ApBehaviorEntry> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApBehaviorEntry::getEntryId, userId.toString());
        ApBehaviorEntry one = this.getOne(queryWrapper);
        if (one==null) { //如果没有就新增
            one = new ApBehaviorEntry();
            // one.setId(0);
            one.setType(1);
            one.setEntryId(userId.toString());
            one.setCreatedTime(new Date());

            this.save(one);
        }
        return one;
    }
}
