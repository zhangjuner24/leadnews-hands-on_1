package com.heima.comment.client;

import com.heima.comment.dto.ApUser;
import com.heima.common.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("leadnews-user")
public interface UserFeign {

    @GetMapping("/api/v1/user/{id}")
    public ResponseResult<ApUser> getById(@PathVariable("id") Integer id);
}
