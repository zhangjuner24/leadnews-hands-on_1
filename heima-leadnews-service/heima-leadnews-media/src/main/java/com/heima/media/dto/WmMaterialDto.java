package com.heima.media.dto;

import com.heima.common.dto.PageRequestDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class WmMaterialDto extends PageRequestDto {
    /**
     * 收藏状态
     */
    private Integer isCollection; //1 查询收藏的
}
