package com.heima.media.test;

import com.alibaba.fastjson.JSON;
import com.heima.common.dto.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class JsonTest {

    public static void main(String[] args) {
        // String str =  "{\"name\":\"zhangsan\",\"userId\":12}";
        // HashMap user = JSON.parseObject(str, HashMap.class);
        // System.out.println("user = " + user);


        // String str =  "[{\"name\":\"zhangsan\",\"userId\":12},{\"name\":\"lisi\",\"userId\":34},{\"name\":\"wangwu\"}]";
        // List<User> users = JSON.parseArray(str,User.class);
        // System.out.println("user = " + users);

        AtomicInteger ai = new AtomicInteger(0);

        while (true){
            ai.incrementAndGet();
            boolean b = ai.compareAndSet(9, 10);
            if(b){
                System.out.println(ai.get());
                break;
            }
        }



    }
}
