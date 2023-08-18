package com.heima.user.dto;

import com.heima.common.dto.PageRequestDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AuthDto extends PageRequestDto {
    /**
     * 状态
     */
    private Integer status;

    private Long id;  //

    private String  msg;
}
