package com.example.demo.config;

import com.example.demo.aop.AccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private  AccessInterceptor accessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessInterceptor);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/toLogin").setViewName("toLogin");
        registry.addViewController("/toRegister").setViewName("toRegister");
        registry.addViewController("/list").setViewName("list");
        registry.addViewController("/info").setViewName("info");
        registry.addViewController("/password").setViewName("password");
    }

    @Bean
    public List<String> getIgnoreUrls() {
        List<String> urls = new ArrayList<>();
        urls.add("/toLogin");
        urls.add("/toRegister");
        urls.add("/list");
        urls.add("/info");
        urls.add("/password");
        return urls;
    }
}
