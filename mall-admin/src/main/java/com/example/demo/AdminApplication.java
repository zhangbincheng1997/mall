package com.example.demo;

import com.example.demo.component.redis.RedisService;
import com.example.demo.utils.Constants;
import com.example.demo.entity.Product;
import com.example.demo.mapper.ProductMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@MapperScan({"com.example.demo.mapper", "com.example.demo.dao"}) // 扫描Mapper
public class AdminApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public void run(String... args) throws Exception {
        List<Product> productList = productMapper.selectList(null);
        // 初始化
        productList.forEach(product -> {
            redisService.set(Constants.PRODUCT_STOCK + product.getId(), product.getStock());
        });
    }
}
