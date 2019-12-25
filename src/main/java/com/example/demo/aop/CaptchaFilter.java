package com.example.demo.aop;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.RedisService;
import com.example.demo.utils.RenderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    private RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (request.getMethod().equals("POST") && request.getRequestURI().equals("/login")) {
            // 获取参数中的验证码
            String key = request.getParameter("key") == null ? "" : request.getParameter("key");
            String code = request.getParameter("code") == null ? "" : request.getParameter("code");
            // 获取redis中的验证码
            String redisCode = (String) redisService.get(key);
            log.info(code + " " + redisCode);
            // 判断验证码
            if (redisCode == null) {
                RenderUtils.render(response, Result.failure(Status.CAPTCHA_EXPIRE));
            } else if (!redisCode.equals(code.toLowerCase())) {
                RenderUtils.render(response, Result.failure(Status.CAPTCHA_ERROR));
            }
        }
        chain.doFilter(request, response);
    }
}
