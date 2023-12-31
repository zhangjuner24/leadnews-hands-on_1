package com.heima.media.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
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
 * 敏感词信息表
 * </p>
 *
 * @author syl
 * @since 2023-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("wm_sensitive")
@ApiModel(description="敏感词信息表")
public class WmSensitive implements Serializable {

    private static final long serialVersionUID = 1L;



    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelProperty("序号")
    private Integer id;

    /**
     * 敏感词
     */
    @ApiModelProperty(value = "敏感词")
    @TableField("sensitives")
    @ExcelProperty("敏感词")
    private String sensitives;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("created_time")
    // @ExcelProperty("创建时间")
    @ExcelIgnore
    private Date createdTime;

    @TableField(exist = false)
    private String name;


}
