package com.example.demo.base;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = GlobalException.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest request, HttpServletResponse response, GlobalException e) {
        log.error(e.getMessage());
        return Result.failure(e.getStatus());
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest request, HttpServletResponse response, BindException e) {
        log.error(e.getMessage());
        List<ObjectError> errorMessages = e.getAllErrors();
        String msg = errorMessages.get(0).toString();
        return Result.failure(Status.BIND_EXCEPTION.getCode(), msg);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest request, HttpServletResponse response, ConstraintViolationException e) {
        log.error(e.getMessage());
        List<String> errorMessages = e.getConstraintViolations()
                .stream()
                .map(cv -> cv.getMessage())
                .collect(Collectors.toList());
        String msg = errorMessages.get(0);
        return Result.failure(Status.BIND_EXCEPTION.getCode(), msg);
    }
}

