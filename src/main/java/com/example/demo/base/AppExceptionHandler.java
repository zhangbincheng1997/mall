package com.example.demo.base;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class AppExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(AppExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        logger.error(e.getMessage());
        e.printStackTrace();
        if (e instanceof BindException) {
            BindException ex = (BindException) e;
            List<ObjectError> errorList = ex.getAllErrors();
            ObjectError error = errorList.get(0);
            String msg = error.getDefaultMessage();
            return Result.message(Status.BIND_EXCEPTION.getCode(), msg);
        } else if (e instanceof RuntimeException) {
            return Result.message(Status.RUNTIME_EXCEPTION.getCode(), e.getMessage());
        } else if (e instanceof IOException) {
            return Result.message(Status.IO_EXCEPTION.getCode(), e.getMessage());
        } else {
            return Result.message(Status.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }
}

