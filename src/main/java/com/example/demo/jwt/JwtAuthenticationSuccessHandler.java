package com.example.demo.jwt;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.example.demo.base.Result;
import com.example.demo.utils.RenderUtils;
import com.example.demo.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthenticationSuccessHandler 登录成功
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
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        BeanUtil.copyProperties(userDetails.getUser(), loginVo, CopyOptions.create().setIgnoreNullValue(true));
        RenderUtils.render(response, Result.success(loginVo));
    }
}
