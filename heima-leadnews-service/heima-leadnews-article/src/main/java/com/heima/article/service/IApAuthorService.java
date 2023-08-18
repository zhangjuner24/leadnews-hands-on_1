package com.heima.article.service;

import com.heima.article.entity.ApAuthor;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.common.dto.ResponseResult;

/**
 * <p>
 * APP文章作者信息表 服务类
 * </p>
 *
 * @author syl
 * @since 2023-04-16
 */
public interface IApAuthorService extends IService<ApAuthor> {

    ResponseResult<ApAuthor> saveApAuthor(ApAuthor apAuthor);
}
