package com.example.demo.base;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        e.printStackTrace();
        if (e instanceof RuntimeException) {
            return Result.error(Status.RUNTIME_EXCEPTION);
        } else if (e instanceof IOException) {
            return Result.error(Status.IO_EXCEPTION);
        } else {
            return Result.error(Status.INTERNAL_SERVER_ERROR);
        }
    }
}

