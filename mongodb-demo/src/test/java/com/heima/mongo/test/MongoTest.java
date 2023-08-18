package com.heima.mongo.test;

import com.heima.mongo.entity.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;

@SpringBootTest
public class MongoTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testAdd() throws Exception {

        Comment comment = new Comment();
        comment.setId("123");
        comment.setUserId(1);
        comment.setUserName("zhangsan");
        comment.setEntryId(123456L);
        comment.setContent("泰库了");
        mongoTemplate.save(comment,"ap_comment_"+comment.getEntryId());
    }


    @Test
    public void testAdd2() throws Exception {

        Comment comment = new Comment();
        comment.setId("12347");
        comment.setUserId(1);
        comment.setUserName("zhangsan4");
        comment.setEntryId(1234546L);
        comment.setContent("泰库了44");
        mongoTemplate.save(comment);


        Comment c2 = new Comment();
        c2.setId("12348");
        c2.setUserId(1);
        c2.setUserName("zhangsan444");
        c2.setEntryId(1234546L);
        c2.setContent("泰库了44");
        mongoTemplate.save(c2);


        Comment c3 = new Comment();
        c3.setId("123459");
        c3.setUserId(1);
        c3.setUserName("zhangsan5");
        c3.setEntryId(1234546L);
        c3.setContent("泰库了44");
        mongoTemplate.save(c3);


    }

    @Test
    public void testUpdate() throws Exception {
        // 根据userId=1修改评论内容
        // Query query = new Query();
        // Criteria criteria = new Criteria();
        // criteria.and("userId").is(1);
        // query.addCriteria(criteria);
        Query query = new Query(Criteria.where("userId").is(1));

        Update update = new Update();
        update.set("content","太美了");

        mongoTemplate.updateFirst(query,update,Comment.class);
        // mongoTemplate.updateMulti(query,update,Comment.class);
    }

    @Test
    public void testQuery() throws Exception {
        // Query query = new Query(Criteria.where("userId").is(1).and("entryId").is(123456L));
        // List<Comment> comments = mongoTemplate.find(query, Comment.class);
        // System.out.println(comments);

        // 每页显示3条数据  第一页
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC,"_id"));
        query.skip(0);
        query.limit(3);
        List<Comment> comments = mongoTemplate.find(query, Comment.class);
        System.out.println(comments);

        Query query2 = new Query();
        query2.with(Sort.by(Sort.Direction.DESC,"_id"));
        query2.skip(3);
        query2.limit(3);
        List<Comment> comments2 = mongoTemplate.find(query2, Comment.class);
        System.out.println(comments2);


    }

    @Test
    public void testDelete() throws Exception {
        Query query = new Query(Criteria.where("userId").is(1).and("entryId").is(123456L));
        mongoTemplate.remove(query,Comment.class);


        // Query query2 = new Query();
        // mongoTemplate.remove(query2,Comment.class);
    }

}
