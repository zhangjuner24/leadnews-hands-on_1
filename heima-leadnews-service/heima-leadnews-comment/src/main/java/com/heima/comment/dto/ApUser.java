package com.heima.comment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * APP用户信息表
 * </p>
 *
 * @author syl
 * @since 2023-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description="APP用户信息表")
public class ApUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Integer id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String name;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String image;



}
