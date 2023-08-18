package com.heima.mongo.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("ap_comment")
@Data
public class Comment {
    /**
     * id
     */
    private String id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 文章id或动态id
     */
    private Long entryId;

    /**
     * 评论内容
     */
    private String content;

}
