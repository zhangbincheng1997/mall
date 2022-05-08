package cn.jnu.common.component.redis;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class RedisService {

    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 批量获取
     */
    public List<Object> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

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
     * 批量设置
     */
    public void multiSet(Map<String, Object> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    /**
     * 删除
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 批量删除
     */
    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
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

    /**
     * hasKey
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * keys
     */
    public Set<String> keys(String key) {
        return redisTemplate.keys(key + "*");
    }

    /**
     * LUA
     */
    public <T> T execute(RedisScript<T> redisScript, List<String> keys, Object... args) {
        return redisTemplate.execute(redisScript, keys, args);
    }
}
