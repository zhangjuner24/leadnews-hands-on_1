package com.heima.search.repository;

import com.heima.search.entity.ApArticleSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleRepository extends ElasticsearchRepository<ApArticleSearch,Long> {
}
