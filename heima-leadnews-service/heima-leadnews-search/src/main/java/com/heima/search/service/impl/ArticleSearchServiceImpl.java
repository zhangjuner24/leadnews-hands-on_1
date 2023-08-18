package com.heima.search.service.impl;

import com.heima.common.dto.ResponseResult;
import com.heima.common.util.UserThreadLocalUtil;
import com.heima.search.dto.ApArticleSearchDto;
import com.heima.search.entity.ApArticleSearch;
import com.heima.search.repository.ArticleRepository;
import com.heima.search.service.IArticleSearchService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleSearchServiceImpl  implements IArticleSearchService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public ResponseResult search(ApArticleSearchDto dto) {

        List<ApArticleSearch> articleSearchList = new ArrayList<>();
        // TODO 4.2 完成文章搜索，返回的数据是一个list集合

        return ResponseResult.okResult(articleSearchList);
    }
}
