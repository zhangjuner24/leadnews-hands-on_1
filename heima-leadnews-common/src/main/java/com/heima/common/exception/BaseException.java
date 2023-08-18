package com.heima.common.exception;

import com.heima.common.dto.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class BaseException {

    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionHandler(Exception e){
        log.error("出现异常");
       e.printStackTrace();
        return ResponseResult.errorResult(500,"系统错误，请联系管理员"+e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseResult exceptionHandler(NullPointerException e){
        log.error("出现异常");
        e.printStackTrace();
        return ResponseResult.errorResult(500,"系统错误，请联系管理员"+e.getMessage());
    }


    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ResponseResult exceptionHandler(ArrayIndexOutOfBoundsException e){
        log.error("出现异常");
        e.printStackTrace();
        return ResponseResult.errorResult(500,"系统错误，请联系管理员"+e.getMessage());
    }




}
