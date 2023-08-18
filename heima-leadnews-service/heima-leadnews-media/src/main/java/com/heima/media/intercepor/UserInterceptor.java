package com.heima.media.intercepor;

import com.heima.common.dto.User;
import com.heima.common.util.UserThreadLocalUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class UserInterceptor implements HandlerInterceptor {
    @Override  // 进入到Controller方法之前
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的userId 和 name
        String userId =  request.getHeader("userId");
        String name =  request.getHeader("name");
        if(StringUtils.isNotBlank(userId)){
            User user = new User(Integer.parseInt(userId),name);
            // 放到ThreadLocal中
            UserThreadLocalUtil.set(user);
        }
        return true;  //一概全部放行
    }

    @Override  // 从Controller方法出来之后
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserThreadLocalUtil.remove();
    }

}
