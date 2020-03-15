package com.example.demo.component;

import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisLocker {

    @Autowired
    private RedissonClient redissonClient;

    public RLock lock(String lockKey) {
        return lock(lockKey, -1L);
    }

    public RLock lock(String lockKey, long leaseTime) {
        return lock(lockKey, leaseTime, TimeUnit.SECONDS);
    }

    public RLock lock(String lockKey, long leaseTime, TimeUnit timeUnit) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, timeUnit);
        return lock;
    }

    public RedissonMultiLock multiLock(String... lockKey) {
        return multiLock(-1L, lockKey);
    }

    public RedissonMultiLock multiLock(long leaseTime, String... lockKey) {
        return multiLock(leaseTime, TimeUnit.SECONDS, lockKey);
    }

    public RedissonMultiLock multiLock(long leaseTime, TimeUnit timeUnit, String... lockKey) {
        RLock[] rLocks = new RLock[lockKey.length];
        for (int i = 0, length = lockKey.length; i < length; i++) {
            RLock lock = redissonClient.getLock(lockKey[i]);
            rLocks[i] = lock;
        }
        RedissonMultiLock multiLock = new RedissonMultiLock(rLocks);
        multiLock.lock(leaseTime, timeUnit);
        return multiLock;
    }

    public boolean tryLock(String lockKey, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    public void unlock(RLock lock) {
        lock.unlock();
    }

    public boolean isLocked(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        return lock.isLocked();
    }
}
