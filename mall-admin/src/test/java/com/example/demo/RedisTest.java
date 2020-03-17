package com.example.demo;

import com.example.demo.component.redis.RedisService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisService redisService;

    @Test
    public void testDemo() {
        redisService.set("school", "xmu");
        Assert.assertEquals("xmu", redisService.get("school"));
    }
}
