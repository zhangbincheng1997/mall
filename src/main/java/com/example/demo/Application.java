package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-using-springbootapplication-annotation.html
 * @SpringBootApplication: same as @Configuration @EnableAutoConfiguration @ComponentScan
 * @EnableAutoConfiguration: enable Spring Boot’s auto-configuration mechanism
 * @ComponentScan: enable @Component scan on the package where the application is located
 * @Configuration: allow to register extra beans in the context or import additional configuration classes
 */
@SpringBootApplication
@MapperScan("com.example.demo.mapper") // 扫描Mapper
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
