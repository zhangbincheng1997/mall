package com.example.demo;

import com.example.demo.component.RedisService;
import com.example.demo.entity.User;
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
    public void testRedis() {
        redisService.set("school", "xmu");
        Assert.assertEquals("xmu", redisService.get("school"));
    }

    @Test
    public void testRedisObj() {
        User user = new User();
        user.setMobile("account");
        user.setPassword("password");

        redisService.set("user", user);
        Assert.assertEquals(user.toString(), redisService.get("user").toString());
    }
}
