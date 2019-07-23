package com.example.demo;

import com.example.demo.base.RedisBase;
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
    private RedisBase redisBase;

    @Test
    public void testRedis() {
        redisBase.set("school", "xmu");
        Assert.assertEquals("xmu", redisBase.get("school"));
    }

    @Test
    public void testRedisObj() {
        User user = new User();
        user.setAccount("account");
        user.setPassword("password");
        user.setNickname("nickname");

        redisBase.setObj("user", user);
        Assert.assertEquals(user.toString(), redisBase.getObj("user").toString());
    }
}
