package com.example.demo.config;

import cn.hutool.core.util.ArrayUtil;
import com.example.demo.filter.CaptchaFilter;
import com.example.demo.filter.TokenFilter;
import com.example.demo.jwt.JwtAccessDeniedHandler;
import com.example.demo.jwt.JwtAuthenticationEntryPoint;
import com.example.demo.jwt.JwtAuthenticationFailureHandler;
import com.example.demo.jwt.JwtAuthenticationSuccessHandler;
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

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 允许 @PreAuthorize("hasAuthority/hasRole('xxxx')")
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CaptchaFilter captchaFilter;

    @Autowired
    private TokenFilter tokenFilter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Autowired
    private JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler; // 认证成功
    @Autowired
    private JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler; // 认证失败
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // 权限不足 认证入口点
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler; // 权限不足 拒绝访问

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
                .antMatchers("/login", "/register", "/captcha").permitAll() // 登陆注册
                .antMatchers("/return", "/notify").permitAll() // 支付回调
                .antMatchers(HttpMethod.GET, "/product", "/category", "/test").permitAll()
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
        provider.setHideUserNotFoundExceptions(false); // 捕获UsernameNotFoundException
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
