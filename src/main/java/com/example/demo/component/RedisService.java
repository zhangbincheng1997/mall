package com.example.demo.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    public static Integer ONE_DAY_EXPIRE = 24 * 60 * 60;

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, long expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }

    public void hasKey(String key) {
        redisTemplate.hasKey(key);
    }


    public void incr(String key) {
        redisTemplate.opsForValue().increment(key);
    }

    public void decr(String key) {
        redisTemplate.opsForValue().decrement(key);
    }
}
