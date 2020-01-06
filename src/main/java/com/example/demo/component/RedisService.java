package com.example.demo.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置
     */
    public void set(String key, Object value, long expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * 设置
     */
    public void set(String key, Object value, long expire, TimeUnit timeUint) {
        redisTemplate.opsForValue().set(key, value, expire, timeUint);
    }

    /**
     * 获取
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * value + delta
     */
    public Long increment(String key, int delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * value - delta
     */
    public Long decrement(String key, int delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }
}
