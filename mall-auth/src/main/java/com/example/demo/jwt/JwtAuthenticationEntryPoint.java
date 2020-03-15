package com.example.demo.jwt;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.utils.RenderUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthenticationEntryPoint 权限不足 认证入口点
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) throws IOException, ServletException {
        RenderUtil.render(response, Result.failure(Status.AUTHENTICATION_ENTRY_POINT));
    }
}
