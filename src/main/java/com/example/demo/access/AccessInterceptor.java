package com.example.demo.access;

import com.alibaba.fastjson.JSON;
import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.RedisService;
import com.example.demo.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // API访问限制
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }

            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();

            if (seconds != 0 && maxCount != 0) {
                String key = Constants.ACCESS_KEY + request.getRequestURL();
                Integer count = (Integer) redisService.get(key);
                if (count == null) {
                    redisService.set(key, Integer.valueOf(1), seconds);
                } else if (count < maxCount) {
                    redisService.increment(key, 1);
                } else {
                    render(response, Status.ACCESS_LIMIT);
                    return false;
                }
            }
        }
        return true;
    }

    // 输出错误
    private void render(HttpServletResponse response, Status status) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(Result.failed(status));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
