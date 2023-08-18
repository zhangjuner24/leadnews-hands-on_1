package com.heima.media.service;

import com.heima.media.entity.WmNews;

public interface IAuditService {
    void audit(WmNews wmNews);

    void audit(Integer id);

    public Long creatApArticle(WmNews wmNews);
}
