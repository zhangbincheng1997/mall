package cn.jnu.security.config;

import cn.hutool.core.util.ArrayUtil;
import cn.jnu.security.filter.CaptchaFilter;
import cn.jnu.security.filter.TokenFilter;
import cn.jnu.security.jwt.JwtAccessDeniedHandler;
import cn.jnu.security.jwt.JwtAuthenticationEntryPoint;
import cn.jnu.security.jwt.JwtAuthenticationFailureHandler;
import cn.jnu.security.jwt.JwtAuthenticationSuccessHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 权限控制 @PreAuthorize("hasAuthority/hasRole('xxxx')")
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CaptchaFilter captchaFilter;
    private final IgnoreUrlsConfig ignoreUrlsConfig;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler; // 权限不足 拒绝访问
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // 权限不足 认证入口点
    private final JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler; // 认证失败
    private final JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler; // 认证成功
    private final PasswordEncoder passwordEncoder;
    private final TokenFilter tokenFilter;
    private final UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 白名单
        for (String url : ArrayUtil.toArray(ignoreUrlsConfig.getUrls(), String.class)) {
            http.authorizeRequests().antMatchers(url).permitAll();
        }
        // 配置参数
        http
                .csrf().disable() // 关闭csrf
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 关闭session
                .and().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 跨域会请求OPTIONS
                .antMatchers("/login", "/register", "/captcha").permitAll() // 登录注册
                .antMatchers(HttpMethod.GET, "/test").permitAll()
                .anyRequest().authenticated() // 其他全部需要认证
                .and().formLogin().loginProcessingUrl("/login") // 处理登录请求
                .successHandler(jwtAuthenticationSuccessHandler)
                .failureHandler(jwtAuthenticationFailureHandler)
                .and().exceptionHandling() // 处理异常
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);
        // 请求顺序 CaptchaFilter -> TokenFilter -> UsernamePasswordAuthenticationFilter
        // http.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 禁用缓存
        http.headers().cacheControl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(false); // 捕获UsernameNotFoundException
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
