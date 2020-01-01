package com.example.demo.base;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    public Result<String> exceptionHandler(HttpServletRequest request, HttpServletResponse response, ConstraintViolationException e) {
        log.error(e.getMessage());
        List<String> errorList = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        String msg = errorList.get(0);
        return Result.failure(Status.CONSTRAINT_VIOLATION_EXCEPTION.getCode(), msg);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public Result<String> exceptionHandler(HttpServletRequest request, HttpServletResponse response, HttpMessageNotReadableException e) {
        log.error(e.getMessage());
        String msg = Objects.requireNonNull(e.getRootCause()).getMessage();
        return Result.failure(Status.HTTP_MESSAGE_NOT_READABLE_EXCEPTION.getCode(), msg);
    }
}

