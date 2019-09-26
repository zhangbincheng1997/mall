package com.example.demo.access;

import com.example.demo.base.Result;
import com.example.demo.utils.RenderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 实现接口AuthenticationSuccessHandler，登录成功，把用户信息存入SecurityContextHolder，并且生成token返回给前端。
 */
@Slf4j
@Component
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication); // 保存上下文
        String token = jwtTokenService.generateToken(userDetails.getUsername()); // 生成jwt
        RenderUtils.render(httpServletResponse, Result.success(token));
        log.info("成功"+token);
    }
}
