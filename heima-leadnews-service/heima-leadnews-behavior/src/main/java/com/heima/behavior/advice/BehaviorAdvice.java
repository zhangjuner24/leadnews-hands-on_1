package com.heima.behavior.advice;


import com.heima.behavior.dto.BehaviorDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Aspect  //声明当前类是一个切面类
public class BehaviorAdvice {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Around("execution(* com.heima.behavior.service.impl..*.*(..) )")
    public Object saveBehaviorToRedis(ProceedingJoinPoint pjp){



        Object object = null;
        try {
            object = pjp.proceed();

            MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
            String methodName = methodSignature.getMethod().getName(); //获取方法名称


            // methodSignature.getMethod().isAnnotationPresent(ToRedis.class)
            if (methodName.startsWith("save")) {

                BehaviorDto dto = (BehaviorDto) pjp.getArgs()[0];  //获取方法的参数
                Integer userId = dto.getUserId();
                Integer authorId = dto.getAuthorId();
                Long articleId = dto.getArticleId();

                // 操作类型 0  关注 点赞 不喜欢 收藏    1 取消关注 取消点赞 取消不喜欢 取消收藏

                if(dto.getOperation()==0){
                    redisTemplate.boundValueOps(methodName+"_"+userId+"_"+authorId+"_"+articleId).set("");
                }else{
                    redisTemplate.delete(methodName+"_"+userId+"_"+authorId+"_"+articleId);
                }
            }



        } catch (Throwable throwable) {
           throw new RuntimeException(throwable);
        }

        return object;

    }

}
