package com.heima.comment.controller;

import com.heima.comment.dto.CommentDto;
import com.heima.comment.dto.CommentLikeDto;
import com.heima.comment.dto.CommentSaveDto;
import com.heima.comment.service.ICommentService;
import com.heima.common.dto.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @PostMapping("/save")
    public ResponseResult saveComment(@RequestBody CommentSaveDto dto){

        return commentService.saveComment(dto);
    }

    @PostMapping("/load")
    @ApiOperation("查询评论列表")
    public ResponseResult loadComment(@RequestBody CommentDto dto){

        return commentService.loadComment(dto);
    }

    @PostMapping("/like")
    @ApiOperation("对评论点赞或取消点赞")
    public ResponseResult commentLike(@RequestBody CommentLikeDto dto){

        return commentService.commentLike(dto);
    }
}
