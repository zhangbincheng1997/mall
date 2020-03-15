package com.example.demo.jwt;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.utils.RenderUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AccessDeniedHandler 权限不足 拒绝访问
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {
        RenderUtil.render(response, Result.failure(Status.ACCESS_DENIED));
    }
}
