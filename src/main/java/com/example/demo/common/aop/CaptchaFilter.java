package com.example.demo.common.aop;

import com.example.demo.common.base.Result;
import com.example.demo.common.base.Status;
import com.example.demo.component.RedisService;
import com.example.demo.common.utils.RenderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    private RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (request.getMethod().equals("POST") &&
                (request.getRequestURI().equals("/login") || request.getRequestURI().equals("/register"))) {
            // 获取参数中的验证码
            String key = request.getParameter("key") == null ? "" : request.getParameter("key");
            String code = request.getParameter("code") == null ? "" : request.getParameter("code");
            // 获取缓存中的验证码
            String redisCode = (String) redisService.get(key);
            // 判断验证码
            if (redisCode == null) {
                RenderUtil.render(response, Result.failure(Status.CAPTCHA_EXPIRE));
            } else if (!redisCode.equals(code.toLowerCase())) {
                RenderUtil.render(response, Result.failure(Status.CAPTCHA_ERROR));
            }
        }
        chain.doFilter(request, response);
    }
}
