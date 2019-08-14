package com.example.demo.config;

import com.example.demo.access.UserArgumentResolver;
import com.example.demo.access.AccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    UserArgumentResolver userArgumentResolver;

    @Autowired
    AccessInterceptor accessInterceptor;

    // controller参数赋值
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessInterceptor);
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/upload/**").addResourceLocations("classpath:/upload/");
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        String[] excludes = new String[]{"/", "/login", "/register", "/static/**", "/upload/**",
//                "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**"};
//        registry.addInterceptor(new AccessInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns(excludes);
//    }
}
