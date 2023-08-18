package com.heima.behavior.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * APP不喜欢行为表
 * </p>
 *
 * @author syl
 * @since 2023-04-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ap_unlikes_behavior")
@ApiModel(description="APP不喜欢行为表")
public class ApUnlikesBehavior implements Serializable {

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
     * 文章ID
     */
    @ApiModelProperty(value = "文章ID")
    @TableField("article_id")
    private Long articleId;

    /**
     * 0 不喜欢
            1 取消不喜欢
     */
    @ApiModelProperty(value = "0 不喜欢            1 取消不喜欢")
    @TableField("operation")
    private Integer operation;

    /**
     * 登录时间
     */
    @ApiModelProperty(value = "登录时间")
    @TableField("created_time")
    private Date createdTime;


}
