package cn.jnu.common.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = GlobalException.class)
    public Result<String> exceptionHandler(HttpServletRequest request, HttpServletResponse response, GlobalException e) {
        log.error(e.getMessage());
        return Result.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = BindException.class)
    public Result<String> exceptionHandler(HttpServletRequest request, HttpServletResponse response, BindException e) {
        log.error(e.getMessage());
        List<ObjectError> errorList = e.getAllErrors();
        String message = errorList.get(0).getDefaultMessage();
        return Result.failure(ResultCode.BIND_EXCEPTION.getCode(), message);
    }
}

