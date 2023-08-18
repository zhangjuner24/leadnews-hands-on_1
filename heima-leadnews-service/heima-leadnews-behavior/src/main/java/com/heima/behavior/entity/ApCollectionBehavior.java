package com.heima.behavior.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * APP收藏信息表
 * </p>
 *
 * @author syl
 * @since 2023-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ap_collection_behavior")
@ApiModel(description="APP收藏信息表")
public class ApCollectionBehavior implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 实体ID
     */
    @ApiModelProperty(value = "实体ID")
    @TableField("entry_id")
    private Integer entryId;

    /**
     * 0 收藏
            1 取消收藏
     */
    @ApiModelProperty(value = "0 收藏            1 取消收藏")
    @TableField("operation")
    private Integer operation;

    /**
     * 文章ID
     */
    @ApiModelProperty(value = "文章ID")
    @TableField("article_id")
    private Long articleId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("collection_time")
    private Date collectionTime;


}
