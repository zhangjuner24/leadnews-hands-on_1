package com.heima.user.client;

import com.heima.common.dto.ResponseResult;
import com.heima.user.dto.ApAuthor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("leadnews-article")
public interface ArticleFeign {

    @PostMapping("/api/v1/author")
    public ResponseResult<ApAuthor> saveApAuthor(@RequestBody ApAuthor apAuthor);
}
