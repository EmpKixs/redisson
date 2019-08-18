/*
 * Copyright (c) 2019.
 * hnf Co.Ltd. 2002-
 * All rights resolved
 */
package com.kixs.redis.redisson;

import com.kixs.redis.RedisApplication;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * TODO File Description
 *
 * @author wangbing
 * @version v1.0.0
 * @date 2019/8/16 18:20
 */
@Slf4j
@RestController
@RequestMapping("/redisson")
public class RedissonController {

    private static final String LOCK_PREFIX = "lock:";

    private static String buildLockKey(String key) {
        return LOCK_PREFIX + key;
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @PostMapping("")
    public String add(@RequestParam("key") String key, @RequestParam("value") String value) {
        RLock lock = redissonClient.getFairLock(buildLockKey(key));
        boolean locked = false;
        try {
            locked = lock.tryLock(10, TimeUnit.SECONDS);
            if (locked) {
                stringRedisTemplate.opsForValue().set(key, value);
            }
        } catch (Exception e) {
            log.error( "异常",e);
            e.printStackTrace();
        } finally {
            if (locked) {
                lock.unlock();
            }
        }

        return locked ? RedisApplication.SUCCESS : RedisApplication.FAILURE;
    }

    @DeleteMapping("")
    public String del(@RequestParam("key") String key) {
        RLock lock = redissonClient.getFairLock(buildLockKey(key));
        boolean locked = false;
        try {
            locked = lock.tryLock(10, TimeUnit.SECONDS);
            if (locked) {
                stringRedisTemplate.delete(key);
            }
        } catch (Exception e) {
            log.error( "异常",e);
            e.printStackTrace();
        } finally {
            if (locked) {
                lock.unlock();
            }
        }

        return locked ? RedisApplication.SUCCESS : RedisApplication.FAILURE;
    }

    @GetMapping("")
    public String get(@RequestParam("key") String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }
}
