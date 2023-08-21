package com.heima.article.task;

import com.heima.article.service.IApArticleService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// @Component
public class HotArticleTask {

    @Autowired
    private IApArticleService apArticleService;

    @Scheduled(cron = "0 0 1 * * ?") //凌晨一点整
    // @Scheduled(cron = "0/30 * * * * ?") //测试 每10秒执行一次
    // @XxlJob("computeHotArticleHandler")
    public ReturnT<String> computeHotArticle(String param){

        try {
            apArticleService.computeHotArticle();
            return  ReturnT.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return  ReturnT.FAIL;
        }
    }

}
