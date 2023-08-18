package com.heima.user.client;

import com.heima.common.dto.ResponseResult;
import com.heima.user.dto.WmUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("leadnews-media")
public interface MediaFeign {

    @PostMapping("/api/v1/user")
    public ResponseResult<WmUser> saveWmUser(@RequestBody WmUser wmUser);
    @PutMapping("/api/v1/user")
    public ResponseResult<WmUser> updateWmUser(@RequestBody WmUser wmUser);
}
