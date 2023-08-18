package com.heima.article.respository;

import com.heima.article.dto.ApArticleSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleRepository extends ElasticsearchRepository<ApArticleSearch,Long> {
}
