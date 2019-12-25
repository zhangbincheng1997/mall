package com.example.demo.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Slf4j
@Controller
public class AppErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @Autowired
    public AppErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    /**
     * HTML页面的错误处理
     */
    @RequestMapping(value = ERROR_PATH, produces = "text/html")
    public String errorPageHandler(HttpServletRequest request, HttpServletResponse response) {
        int status = response.getStatus();
        log.error("错误请求 : " + status);
        switch (status) {
            case 404:
                return "error/404";
            case 500:
                return "error/500";
            default:
                return "error/unknown";
        }
    }

    /**
     * 非HTML页面的错误处理
     */
    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    public Result errorApiHandler(HttpServletRequest request, HttpServletResponse response) {
        int status = response.getStatus();
        log.error("错误请求 : " + status);
        WebRequest webRequest = new ServletWebRequest(request);
        Map<String, Object> attr = this.errorAttributes.getErrorAttributes(webRequest, false);
        return Result.failure(status, String.valueOf(attr.getOrDefault("message", "error")));
    }
}
