package com.example.demo;

import com.example.demo.component.RedisService;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.Product;
import com.example.demo.model.ProductExample;
import com.example.demo.utils.Constants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

/**
 * https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-using-springbootapplication-annotation.html
 *
 * @SpringBootApplication: same as @Configuration @EnableAutoConfiguration @ComponentScan
 * @EnableAutoConfiguration: enable Spring Boot’s auto-configuration mechanism
 * @ComponentScan: enable @Component scan on the package where the application is located
 * @Configuration: allow to register extra beans in the context or import additional configuration classes
 */
@SpringBootApplication
@MapperScan("com.example.demo.mapper") // 扫描Mapper
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public void run(String... args) throws Exception {
        List<Product> productList = productMapper.selectByExample(new ProductExample());
        // 初始化
        productList.forEach(product -> {
            redisService.set(Constants.REDIS_PRODUCT_STOCK + product.getId(), product.getStock());
        });
    }
}
