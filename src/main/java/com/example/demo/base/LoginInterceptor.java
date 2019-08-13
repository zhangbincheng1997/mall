package com.example.demo.base;

import com.alibaba.fastjson.JSON;
import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.entity.User;
import com.example.demo.utils.Constants;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute(Constants.USER_SESSION_KEY);
        if (user == null) {
            // 返回json状态码
            response.setContentType("application/json;charset=UTF-8");
            OutputStream out = response.getOutputStream();
            String str = JSON.toJSONString(Result.error(Status.NOT_LOGIN));
            out.write(str.getBytes("UTF-8"));
            out.flush();
            out.close();
            return false;
        }
        return true;
    }
}
