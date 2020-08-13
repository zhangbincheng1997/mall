package com.example.demo;

import com.example.demo.component.redis.RedisService;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import com.example.demo.utils.Constants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@MapperScan({"com.example.demo.mapper", "com.example.demo.dao"}) // 扫描Mapper
public class AppApplication implements CommandLineRunner {

    @Autowired
    private ProductService productService;

    @Autowired
    private RedisService redisService;

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<Product> productList = productService.list();
        Map<String, Object> stockMap = new HashMap<>();
        productList.forEach(product -> stockMap.put(Constants.PRODUCT_STOCK + product.getId(), product.getStock()));
        redisService.multiSet(stockMap);
    }
}
