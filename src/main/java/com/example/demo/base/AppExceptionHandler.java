package com.example.demo.base;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
		if(e instanceof GlobalException) {
			GlobalException ex = (GlobalException) e;
			return Result.failure(ex.getStatus());
		} else if (e instanceof BindException) {
            BindException ex = (BindException) e;
            List<ObjectError> errorList = ex.getAllErrors();
            ObjectError error = errorList.get(0);
            String msg = error.getDefaultMessage();
            return Result.failure(Status.BIND_EXCEPTION.getCode(), msg);
        } else {
            return Result.failure(Status.INTERNAL_SERVER_ERROR);
        }
    }

    /*@ExceptionHandler(value = BindException.class)
    @ResponseBody
    public Result bindExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        BindException ex = (BindException) e;
        List<ObjectError> errorList = ex.getAllErrors();
        ObjectError error = errorList.get(0);
        String msg = error.getDefaultMessage();
        return Result.failure(Status.BIND_EXCEPTION.getCode(), msg);
    }*/
}

