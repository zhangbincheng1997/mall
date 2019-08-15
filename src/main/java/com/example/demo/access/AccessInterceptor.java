package com.example.demo.access;

import com.alibaba.fastjson.JSON;
import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.RedisService;
import com.example.demo.entity.User;
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
            if (accessLimit.needLogin()) {
                User user = getUser(request, response);
                if (user == null) {
                    render(response);
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
    private void render(HttpServletResponse response) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(Status.NOT_LOGIN));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
