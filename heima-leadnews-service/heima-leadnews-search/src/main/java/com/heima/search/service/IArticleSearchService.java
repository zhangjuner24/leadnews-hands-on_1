package com.heima.search.service;

import com.heima.common.dto.ResponseResult;
import com.heima.search.dto.ApArticleSearchDto;

public interface IArticleSearchService {
    ResponseResult search(ApArticleSearchDto dto);
}
