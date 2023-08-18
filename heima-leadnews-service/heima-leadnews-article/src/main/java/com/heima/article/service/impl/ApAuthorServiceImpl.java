package com.heima.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.article.entity.ApAuthor;
import com.heima.article.mapper.ApAuthorMapper;
import com.heima.article.service.IApAuthorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.dto.ResponseResult;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * APP文章作者信息表 服务实现类
 * </p>
 *
 * @author syl
 * @since 2023-04-16
 */
@Service
public class ApAuthorServiceImpl extends ServiceImpl<ApAuthorMapper, ApAuthor> implements IApAuthorService {

    @Override
    public ResponseResult<ApAuthor> saveApAuthor(ApAuthor apAuthor) {
        //        判断这个app用户是否已经成为文章作者
        LambdaQueryWrapper<ApAuthor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ApAuthor::getUserId,apAuthor.getUserId());
        ApAuthor one = this.getOne(queryWrapper);
        if(one!=null){ //如果成为自媒体人直接返回
            return ResponseResult.okResult(one);
        }else{  //如果没有成为自媒体人 ，新增
            apAuthor.setCreatedTime(new Date());
            this.save(apAuthor);
            return ResponseResult.okResult(apAuthor);
        }
    }
}
