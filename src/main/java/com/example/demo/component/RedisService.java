package com.example.demo.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Order(1)
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置
     *
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置
     *
     * @param key
     * @param value
     * @param expire
     */
    public void set(String key, Object value, long expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * 获取
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除
     *
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * value + delta
     *
     * @param key
     * @return
     */
    public Long increment(String key, int delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * value - delta
     *
     * @param key
     * @return
     */
    public Long decrement(String key, int delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }
}
