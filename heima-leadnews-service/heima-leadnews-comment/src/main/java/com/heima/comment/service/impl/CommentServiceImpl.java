package com.heima.comment.service.impl;

import com.heima.comment.client.UserFeign;
import com.heima.comment.dto.ApUser;
import com.heima.comment.dto.CommentDto;
import com.heima.comment.dto.CommentLikeDto;
import com.heima.comment.dto.CommentSaveDto;
import com.heima.comment.entity.ApComment;
import com.heima.comment.entity.ApCommentLike;
import com.heima.comment.service.ICommentService;
import com.heima.comment.vo.ApCommentVo;
import com.heima.common.dto.ResponseResult;
import com.heima.common.dto.User;
import com.heima.common.enums.AppHttpCodeEnum;
import com.heima.common.util.UserThreadLocalUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements ICommentService {
    @Autowired
    private UserFeign userFeign;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public ResponseResult saveComment(CommentSaveDto dto) {

        User user = UserThreadLocalUtil.get();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        if(StringUtils.isBlank(dto.getContent()) || dto.getContent().length()>140){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        // 远程调用用户信息
        ApUser apUser = userFeign.getById(user.getUserId()).getData();

        ApComment apComment = new ApComment();
        // apComment.setId("");
        apComment.setUserId(user.getUserId());
        apComment.setUserName(apUser.getName());
        apComment.setImage(apUser.getImage());
        apComment.setArticleId(dto.getArticleId());
        apComment.setContent(dto.getContent());
        apComment.setLikes(0);
        apComment.setRepay(0);
        apComment.setCreatedTime(new Date());

        mongoTemplate.save(apComment);



        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult loadComment(CommentDto dto) {

        User user = UserThreadLocalUtil.get();
        Query query = new Query(Criteria.where("articleId").is(dto.getArticleId()).and("createdTime").lt(dto.getMinDate()));
        query.with(Sort.by(Sort.Direction.DESC,"createdTime"));//排序
        query.limit(10);
        List<ApComment> commentList = mongoTemplate.find(query, ApComment.class);
        List<ApCommentVo> commentVoList = new ArrayList<>();
        for (ApComment apComment : commentList) {
            ApCommentVo apCommentVo = new ApCommentVo();
            BeanUtils.copyProperties(apComment,apCommentVo);
            // 当前登录人对这个评论是否点赞
            if (user!=null||user.getUserId()!=0) {
                if (redisTemplate.boundHashOps("comment_like_"+user.getUserId()).hasKey(apComment.getId())) {
                    apCommentVo.setOperation(0);//当前登录人对这个评论是否点赞  点赞：0  没有点赞1
                }else{
                    apCommentVo.setOperation(1);//当前登录人对这个评论是否点赞  点赞：0  没有点赞1
                }
            }
            commentVoList.add(apCommentVo);
        }


        return ResponseResult.okResult(commentVoList);
    }

    @Override  //对评论点赞或取消点赞
    public ResponseResult commentLike(CommentLikeDto dto) {
        User user = UserThreadLocalUtil.get();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        // 保存点赞信息
        ApCommentLike apCommentLike = new ApCommentLike();
        apCommentLike.setUserId(user.getUserId());
        apCommentLike.setCommentId(dto.getCommentId());
        apCommentLike.setOperation(dto.getOperation());
        apCommentLike.setCreateTime(new Date());
        mongoTemplate.save(apCommentLike);

        // 修改评论收到的点赞数  点赞： update ap_comment  set likes=likes+1 where id=?
        //                   取消点赞： update ap_comment  set likes=likes-1 where id=?
        Query query = new Query(Criteria.where("_id").is(dto.getCommentId()));

        Update update = new Update();
        if(dto.getOperation()==0){  //点赞
            update.inc("likes",1);
            // 向Redis中存放标记 当前登录人对哪个评论点赞
            redisTemplate.boundHashOps("comment_like_"+user.getUserId()).put(dto.getCommentId(),"");
        }else{
            update.inc("likes",-1); //取消点赞
            redisTemplate.boundHashOps("comment_like_"+user.getUserId()).delete(dto.getCommentId());
        }
        mongoTemplate.updateFirst(query,update,ApComment.class);
        return ResponseResult.okResult();
    }

}
