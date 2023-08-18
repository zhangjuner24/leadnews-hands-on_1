package com.heima.search.dto;

import com.heima.common.dto.PageRequestDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApArticleSearchDto extends PageRequestDto {

    // 设备ID
    String equipmentId;
    // 关键字
    String searchWords;

    List<ApUserSearch> hisList;

}
