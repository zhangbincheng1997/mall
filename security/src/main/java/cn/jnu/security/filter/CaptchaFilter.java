package cn.jnu.security.filter;

import cn.jnu.common.base.Result;
import cn.jnu.common.base.ResultCode;
import cn.jnu.common.component.redis.RedisService;
import cn.jnu.common.utils.RenderUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class CaptchaFilter extends OncePerRequestFilter {

    private final RedisService redisService;

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
                RenderUtil.render(response, Result.failure(ResultCode.CAPTCHA_EXPIRE));
            } else if (!redisCode.equals(code.toLowerCase())) {
                RenderUtil.render(response, Result.failure(ResultCode.CAPTCHA_ERROR));
            }
        }
        chain.doFilter(request, response);
    }
}
