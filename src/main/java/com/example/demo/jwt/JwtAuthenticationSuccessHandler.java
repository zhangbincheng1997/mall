package com.example.demo.jwt;

import cn.hutool.core.convert.Convert;
import com.example.demo.base.Result;
import com.example.demo.utils.RenderUtil;
import com.example.demo.vo.LoginSuccessVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthenticationSuccessHandler 认证成功
 */
@Component
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal(); // SecurityContextHolder上下文
        String token = jwtTokenUtils.generateToken(userDetails.getUsername());
        LoginSuccessVo loginSuccessVo = Convert.convert(LoginSuccessVo.class, userDetails.getUser());
        loginSuccessVo.setToken(token);
        RenderUtil.render(response, Result.success(loginSuccessVo));
    }
}
