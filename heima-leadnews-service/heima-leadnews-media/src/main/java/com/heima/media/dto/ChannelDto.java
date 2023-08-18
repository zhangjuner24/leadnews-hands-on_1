package com.heima.media.dto;

import com.heima.common.dto.PageRequestDto;
import lombok.*;

@Data

@EqualsAndHashCode(callSuper = true) //在做equals时是否比较父类的属性值
public class ChannelDto extends PageRequestDto {
    private String name;
//    public static void main(String[] args) {
//        ChannelDto c1 = new ChannelDto();
//        c1.setPage(1L);
//        c1.setSize(1);
//        c1.setName("a");
//
//        ChannelDto c2 = new ChannelDto();
//        c2.setPage(2L);
//        c2.setSize(1);
//        c2.setName("a");
//
//        System.out.println(c1.equals(c2));
//
//    }
}



