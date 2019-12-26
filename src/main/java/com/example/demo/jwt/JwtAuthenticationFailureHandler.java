package com.example.demo.jwt;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.utils.RenderUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthenticationFailureHandler 认证失败
 */
@Component
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {
        Status status = Status.AUTHENTICATION_FAILURE;
        if (e instanceof UsernameNotFoundException) {
            status = Status.USERNAME_NOT_FOUND;
        } else if (e instanceof BadCredentialsException) {
            status = Status.BAD_CREDENTIALS;
        } else if (e instanceof DisabledException) {
            status = Status.ACCOUNT_DISABLED;
        } else if (e instanceof AccountExpiredException) {
            status = Status.ACCOUNT_EXPIRED;
        } else if (e instanceof LockedException) {
            status = Status.ACCOUNT_LOCKED;
        } else if (e instanceof CredentialsExpiredException) {
            status = Status.CREDENTIALS_EXPIRED;
        }
        RenderUtils.render(response, Result.failure(status));
    }
}
