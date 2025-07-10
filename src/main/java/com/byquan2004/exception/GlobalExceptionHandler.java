package com.byquan2004.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public String handleException(Exception e) {
        return "抱歉除了一点问题，请稍后再尝试吧～";
    }
}
