package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testMySQL() {
        User user = new User();
        user.setAccount("account");
        user.setPassword("password");
        user.setNickname("nickname");

        userRepository.save(user);
        Assert.assertEquals("nickname", userRepository.findById(1L).get().getNickname());
    }

    @Test
    public void testRedis() {
        stringRedisTemplate.opsForValue().set("school", "xmu");
        Assert.assertEquals("xmu", stringRedisTemplate.opsForValue().get("school"));
    }

    @Test
    public void testRedisObj() {
        User user = new User();
        user.setAccount("account");
        user.setPassword("password");
        user.setNickname("nickname");

        ValueOperations<String, User> operations = redisTemplate.opsForValue();
        operations.set("user", user);
        Assert.assertEquals("nickname", operations.get("user").getNickname());
    }
}
