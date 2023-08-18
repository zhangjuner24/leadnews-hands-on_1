package com.heima.freemarker.controller;

import com.heima.freemarker.pojo.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HelloController {

    @RequestMapping("/hello")
    public String hello(HttpServletRequest request){

        // 准备模板需要的数据
        request.setAttribute("name","张三");

        Student student = new Student("lisi",30);

        request.setAttribute("stu",student);


        List<Student> studentList = new ArrayList<Student>();
        studentList.add(new Student("lisi",20));
        studentList.add(new Student("zhangsan",40));
        studentList.add(new Student("jerry",35));
        studentList.add(new Student("jack",39));
        request.setAttribute("stus",studentList);


        Map map = new HashMap();
        map.put("name","张三");
        map.put("age",23);
        map.put("address","广州");
        request.setAttribute("userMap",map);

        return "basic";
    }
}
