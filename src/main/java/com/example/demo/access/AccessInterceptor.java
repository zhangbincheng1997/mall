package com.example.demo.access;

import com.alibaba.fastjson.JSON;
import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.RedisService;
import com.example.demo.model.User;
import com.example.demo.utils.Constants;
import com.example.demo.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
        // 登录限制
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }

            String key = Constants.ACCESS_KEY + "_" + request.getRequestURL();
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            Integer count = (Integer) redisService.get(key);
            if (count == null) {
                redisService.set(key, Integer.valueOf(1), seconds);
            } else if (count < maxCount) {
                redisService.incr(key);
            } else {
                render(response, Status.ACCESS_LIMIT);
                return false;
            }

            if (accessLimit.needLogin()) {
                User user = getUser(request, response);
                if (user == null) {
                    render(response, Status.NOT_LOGIN);
                    return false;
                }
                // 注入对象
                UserContext.setUser(user);
            }
        }
        return true;
    }

    // 获取用户
    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String token = CookieUtils.getCookie(request, Constants.COOKIE_TOKEN);
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        if (redisService.hasKey(token)) {
            // 延长有效期
            User user = (User) redisService.get(token);
            redisService.set(token, user, Constants.COOKIE_EXPIRY);
            CookieUtils.setCookie(response, Constants.COOKIE_TOKEN, token, Constants.COOKIE_EXPIRY);
            return user;
        }
        return null;
    }

    // 输出错误
    private void render(HttpServletResponse response, Status status) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(status));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
