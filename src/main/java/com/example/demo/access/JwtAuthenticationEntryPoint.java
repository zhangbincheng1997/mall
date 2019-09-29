package com.example.demo.access;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.utils.RenderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 实现接口AuthenticationEntryPoint，token失效或者错误，直接返回前端认证失败信息。
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.info("失败：没有登录，token无效"+e.getMessage());
        RenderUtils.render(httpServletResponse, Result.failed(Status.TOKEN_ERROR));
    }
}
