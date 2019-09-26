package com.example.demo.config;

import com.example.demo.access.*;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true) //使用表达式实现方法级别的控制，如：@PreAuthorize("hasRole('ADMIN')")
public class SecurityConfig   extends WebSecurityConfigurerAdapter  {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    @Autowired
    private  JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler; // 成功
    @Autowired
    private  JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler; // 账号密码错误
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // token过期
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler; // 权限不足

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 配置
        http
                .csrf().disable() // 关闭csrf保护功能（跨域访问）
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)// 基于token，所以不需要session
                .and().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 跨域会请求OPTIONS
                .antMatchers(HttpMethod.GET, // 允许对于网站静态资源的无授权访问
                        "/",
                        "/favicon.ico",
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/v2/**",
                        "/webjars/**"
                ).permitAll()
                .antMatchers("/register", "/login", "/updatePassword").permitAll()
//                .antMatchers("/goods/**").hasAnyRole("ADMIN", "TEST")
                .anyRequest().authenticated() // 其他全部需要认证
                .and().formLogin().loginProcessingUrl("/login") // 指定登录请求路径 通过UsernamePasswordAuthenticationFilter
                .successHandler(jwtAuthenticationSuccessHandler)
                .failureHandler(jwtAuthenticationFailureHandler)
                .and().exceptionHandling() // 处理异常
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);

        // 请求顺序：JwtTokenFilter -> UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 禁用缓存
        http.headers().cacheControl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 通过AuthenticationManagerBuilder将我们
     * 自定义的JwtUserDetailsServiceImpl和加密方式BCryptPasswordEncoder进行赋值。
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Autowired
    UserService userService;

    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userService.getUserByUsername(username);
                if (user != null) {
//                List<Permission> permissionList = userService.getPermissionList(user.getId());
                    log.info("成功");
                    return new JwtUserDetails(user.getUsername(), user.getPassword(), null);
                }
                throw new UsernameNotFoundException("用户名或密码错误");
            }
        };
    }
}
