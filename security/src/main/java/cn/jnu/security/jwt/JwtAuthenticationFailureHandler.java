package cn.jnu.security.jwt;

import cn.jnu.common.base.Result;
import cn.jnu.common.base.ResultCode;
import cn.jnu.common.utils.RenderUtil;
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
        ResultCode status = ResultCode.AUTHENTICATION_FAILURE;
        if (e instanceof UsernameNotFoundException) {
            status = ResultCode.USERNAME_NOT_FOUND;
        } else if (e instanceof BadCredentialsException) {
            status = ResultCode.BAD_CREDENTIALS;
        } else if (e instanceof DisabledException) {
            status = ResultCode.ACCOUNT_DISABLED;
        } else if (e instanceof AccountExpiredException) {
            status = ResultCode.ACCOUNT_EXPIRED;
        } else if (e instanceof LockedException) {
            status = ResultCode.ACCOUNT_LOCKED;
        } else if (e instanceof CredentialsExpiredException) {
            status = ResultCode.CREDENTIALS_EXPIRED;
        }
        RenderUtil.render(response, Result.failure(status));
    }
}
