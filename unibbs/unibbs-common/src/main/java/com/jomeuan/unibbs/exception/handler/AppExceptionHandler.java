package com.jomeuan.unibbs.exception.handler;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jomeuan.unibbs.exception.AppException;
import com.jomeuan.unibbs.vo.R;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class AppExceptionHandler {

    /**
     * AppException业务异常处理
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = AppException.class)
    @ResponseBody
    public ResponseEntity<RuntimeException> bizExceptionHandler(AppException e){
        return ResponseEntity.badRequest().body(e);
    }
}
