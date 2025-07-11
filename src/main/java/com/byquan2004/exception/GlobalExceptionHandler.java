package com.byquan2004.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    public String handleRuntimeException(RuntimeException e) {
        return "sorry,the request is too frequent, please try again later";
    }

    @ExceptionHandler(value = Exception.class)
    public String handleException(Exception e) {
        return "server error";
    }
}
