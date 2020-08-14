package com.example.demo.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = GlobalException.class)
    @ResponseBody
    public Result<String> exceptionHandler(HttpServletRequest request, HttpServletResponse response, GlobalException e) {
        log.error(e.getMessage());
        return Result.failure(e.getStatus());
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public Result<String> exceptionHandler(HttpServletRequest request, HttpServletResponse response, BindException e) {
        log.error(e.getMessage());
        List<ObjectError> errorList = e.getAllErrors();
        String msg = errorList.get(0).getDefaultMessage();
        return Result.failure(Status.BIND_EXCEPTION.getCode(), msg);
    }
}

