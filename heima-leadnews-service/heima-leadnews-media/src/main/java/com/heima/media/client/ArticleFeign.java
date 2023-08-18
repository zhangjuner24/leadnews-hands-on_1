package com.heima.media.client;

import com.heima.common.dto.ResponseResult;
import com.heima.media.dto.ArticleDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("leadnews-article")
public interface ArticleFeign {
    @PostMapping("/api/v1/article")
    public ResponseResult<Long> saveApArticle(@RequestBody ArticleDto dto);
}
