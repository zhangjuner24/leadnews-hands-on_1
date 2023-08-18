package com.heima.comment.service;

import com.heima.comment.dto.CommentDto;
import com.heima.comment.dto.CommentLikeDto;
import com.heima.comment.dto.CommentSaveDto;
import com.heima.common.dto.ResponseResult;


public interface ICommentService {
    ResponseResult saveComment(CommentSaveDto dto);

    ResponseResult loadComment(CommentDto dto);

    ResponseResult commentLike(CommentLikeDto dto);
}
