package com.example.demo;

//import com.example.demo.component.RedisLocker;

import com.example.demo.component.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Redis {

    @Autowired
    private RedisService redisService;

//    @Autowired
//    private RedisLocker redisLocker;

    private static final String PRODUCT = "product";
    private static final String LOCK = "lock";

    @Test
    public void init() {
    }

    @Test
    public void buy() {
        redisService.set(PRODUCT, 1000);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
//                redisLocker.lock(LOCK);
                int a = (int) redisService.get(PRODUCT) - 1;
                System.out.println(a);
                    redisService.set(PRODUCT, a);
                    redisService.decrement(PRODUCT, 1);
//                redisLocker.unlock(LOCK);
            });
        }
    }

    @Test
    public void result() {
        System.out.println(redisService.get(PRODUCT));
    }
}
