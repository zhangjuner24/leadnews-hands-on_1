package com.heima.search.interceptor;

import com.heima.common.dto.User;
import com.heima.common.util.UserThreadLocalUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        从请求头中获取userId和name
        String userId = request.getHeader("userId");
        String name = request.getHeader("name");
        if(StringUtils.isNotBlank(userId)){
            User user = new User(Integer.parseInt(userId),name);
            UserThreadLocalUtil.set(user);
        }
        return true; //一直是放行
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserThreadLocalUtil.remove();
    }

}
