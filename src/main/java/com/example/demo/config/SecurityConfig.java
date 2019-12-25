package com.example.demo.config;

import com.example.demo.aop.CaptchaFilter;
import com.example.demo.aop.TokenFilter;
import com.example.demo.base.GlobalException;
import com.example.demo.component.RedisService;
import com.example.demo.jwt.*;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 允许 @PreAuthorize("hasAuthority/hasRole('xxxx')")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CaptchaFilter captchaFilter;
    @Autowired
    private TokenFilter tokenFilter;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler; // 登录成功
    @Autowired
    private JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler; // 登录失败
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // 认证失败
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler; // 权限不足

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 配置参数
        http
                .csrf().disable() // 关闭csrf
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 关闭session
                .and().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 跨域会请求OPTIONS
                .antMatchers(HttpMethod.GET,
                        "/", "/csrf",
                        "/favicon.ico", "/**/*.css", "/**/*.js", "/layui/**",
                        "/swagger-ui.html", "/swagger-resources/**", "/v2/**", "/webjars/**"
                ).permitAll()
                .antMatchers("/druid/**").permitAll()
                .antMatchers("/login", "/register", "/captcha").permitAll()
                .anyRequest().authenticated() // 其他全部需要认证
                .and().formLogin().loginProcessingUrl("/login") // 处理登录请求
                .successHandler(jwtAuthenticationSuccessHandler)
                .failureHandler(jwtAuthenticationFailureHandler)
                .and().exceptionHandling() // 处理异常
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);
        // 请求顺序 CaptchaFilter -> TokenFilter -> UsernamePasswordAuthenticationFilter
        http.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);
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
        provider.setHideUserNotFoundExceptions(false);
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
