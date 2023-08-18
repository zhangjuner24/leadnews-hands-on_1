package com.heima.articel.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heima.article.ArticleApp;
import com.heima.article.dto.ApArticleSearch;
import com.heima.article.entity.ApArticle;
import com.heima.article.respository.ArticleRepository;
import com.heima.article.service.IApArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = ArticleApp.class)
public class EsInitTest {


    @Autowired
    private IApArticleService apArticleService;
     @Autowired
    private ArticleRepository articleRepository;

    @Test  // TODO 4.1 执行这个方法就直接创建了ES索引库，并且完成数据的初始化
    public void createEsIndex() throws Exception{
        // 所有未下架 未删除的app文章
        LambdaQueryWrapper<ApArticle> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ApArticle::getIsDelete,false);
        queryWrapper.eq(ApArticle::getIsDown,false);
        List<ApArticle> list = apArticleService.list(queryWrapper);

        List<ApArticleSearch>  articleSearchList = new ArrayList<>();
        for (ApArticle article : list) {
            ApArticleSearch apArticleSearch  = new ApArticleSearch();
            BeanUtils.copyProperties(article,apArticleSearch);
            articleSearchList.add(apArticleSearch);
        }

        articleRepository.saveAll(articleSearchList);

    }
}
