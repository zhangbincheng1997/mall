package com.example.demo.access;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.RedisService;
import com.example.demo.utils.Constants;
import com.example.demo.utils.RenderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * API访问限制
 */
@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            String key = Constants.ACCESS_KEY + request.getRequestURL();
            Integer count = (Integer) redisService.get(key);
            if (count == null) {
                redisService.set(key, Integer.valueOf(1), seconds);
            } else if (count < maxCount) {
                redisService.increment(key, 1);
            } else {
                RenderUtils.render(response, Result.failure(Status.ACCESS_LIMIT));
                return false;
            }
        }
        return true;
    }
}
