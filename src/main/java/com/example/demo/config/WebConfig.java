package com.example.demo.config;

import com.example.demo.aop.AccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AccessInterceptor accessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
    @Bean
    public String[] getIgnoreUrls() {
        return new String[]{"/", "/csrf", "/favicon.ico", "/**/*.css", "/**/*.js", "/layui/**",
                "/swagger-ui.html", "/swagger-resources/**", "/v2/**", "/webjars/**", "/druid/**", "/test/**"};
    }
}
