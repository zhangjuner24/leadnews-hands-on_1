package com.heima.gateway.filter;

import com.heima.gateway.utils.AppJwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
//       有些url不需要判断有token的  登录
//        需要放行登录的url
        if (request.getURI().getPath().contains("login")) {
            return chain.filter(exchange);//直接放行
        }
//        从请求头中获取token
        String token = request.getHeaders().getFirst("token");
        if(StringUtils.isBlank(token)){
            //       1、是否有token  如果没有token 直接返回401状态码
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete(); //直接返回  不再进入微服务中
        }

//        如果有token需要校验 1、是否能解析 2、是否过期
        try {
            Claims claimsBody = AppJwtUtil.getClaimsBody(token);
            int flag = AppJwtUtil.verifyToken(claimsBody);
//            -1：有效，0：有效，    1：过期，2：过期
            if(flag==-1||flag==0){

//                发送请求到微服时携带userId
              request.mutate().header("userId",claimsBody.get("userId",Integer.class).toString());
              request.mutate().header("name",claimsBody.get("name",String.class));
              return chain.filter(exchange);//token校验成功，直接放行  进入到微服务
            }
        } catch (Exception e) {

        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete(); //直接返回  不再进入微服务中
    }

    @Override
    public int getOrder() { //值越小 优先级越高
        return 0;
    }
}
