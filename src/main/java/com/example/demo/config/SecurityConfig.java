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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
// 允许 @PreAuthorize("hasAuthority('xxxx')") @PreAuthorize("hasRole('xxxx')")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;
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
        // 配置
        http
                .csrf().disable() // 关闭csrf
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 关闭session
                .and().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 跨域会请求OPTIONS
                .antMatchers(HttpMethod.GET,
                        "/", "/csrf",
                        "/favicon.ico", "/css/**", "/js/**", "/layui/**", "/goods/**", "/view/**",
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/v2/**",
                        "/webjars/**"
                ).permitAll()
                .antMatchers("/druid/**").permitAll()
                .antMatchers("/user/register", "/user/login").permitAll()
                .anyRequest().authenticated() // 其他全部需要认证
                .and().formLogin().loginPage("/user/login").loginProcessingUrl("/user/login") // 登录请求 UsernamePasswordAuthenticationFilter
                .successHandler(jwtAuthenticationSuccessHandler)
                .failureHandler(jwtAuthenticationFailureHandler)
                .and().exceptionHandling() // 处理异常
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);

        // 请求顺序 JwtTokenFilter -> UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 禁用缓存
        http.headers().cacheControl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    @Autowired
    private UserService userService;

    @Bean
    public UserDetailsService userDetailsService() {
        // 获取登录用户信息
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                User user = userService.getUserByUsername(username);
                if (user != null) {
                    // TODO
                    List<GrantedAuthority> permissionList = userService.getPermissionList(user.getId());
                    List<GrantedAuthority> roleList = userService.getRoleList(user.getId());
                    permissionList.addAll(roleList);
                    return new JwtUserDetails(user.getUsername(), user.getPassword(), permissionList);
                }
                throw new UsernameNotFoundException("用户名或密码错误");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
