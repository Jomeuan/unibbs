package com.jomeuan.unibbs.exception.handler;


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
    public R<?> bizExceptionHandler(AppException e){
        return R.error(e.getMessage());
    }
}
