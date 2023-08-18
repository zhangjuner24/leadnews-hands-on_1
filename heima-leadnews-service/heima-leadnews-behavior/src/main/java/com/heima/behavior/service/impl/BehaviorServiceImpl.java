package com.heima.behavior.service.impl;

import com.heima.behavior.dto.BehaviorDto;
import com.heima.behavior.service.IBehaviorService;
import com.heima.common.dto.ResponseResult;
import com.heima.common.dto.User;
import com.heima.common.util.UserThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BehaviorServiceImpl implements IBehaviorService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public ResponseResult loadBehavior(BehaviorDto dto) {

        User user = UserThreadLocalUtil.get();
        if (user==null||user.getUserId()==0) {
            return null;
        }


        Map map = new HashMap();
        map.put("isfollow",redisTemplate.hasKey("saveFollow_"+user.getUserId()+"_"+dto.getAuthorId()+"_"+dto.getArticleId()));
        map.put("islike",redisTemplate.hasKey("saveLikes_"+user.getUserId()+"_null_"+dto.getArticleId()));
        map.put("isunlike",redisTemplate.hasKey("saveUnlikes_"+user.getUserId()+"_null_"+dto.getArticleId()));
        map.put("iscollection",redisTemplate.hasKey("saveCollection_"+user.getUserId()+"_null_"+dto.getArticleId()));


        return ResponseResult.okResult(map);
    }
}
