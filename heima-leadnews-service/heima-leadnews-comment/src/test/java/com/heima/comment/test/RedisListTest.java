package com.heima.comment.test;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisListTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testList(){
        for (int i = 0; i < 30; i++) {
            redisTemplate.boundListOps("zhuanjiaId").leftPush("1");
           /* redisTemplate.opsForList().leftPush("zhuanjiaId","1");


            redisTemplate.boundValueOps("").set("");
            redisTemplate.opsForValue().set("key","value");*/
        }


        String id = redisTemplate.boundListOps("zhuanjiaId").rightPop();

        System.out.println(id);

        String id1 = redisTemplate.boundListOps("zhuanjiaId").rightPop();
        System.out.println(id1);

        ObjectId objectId = new ObjectId();

    }


}
