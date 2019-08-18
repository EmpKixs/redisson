/*
 * Copyright (c) 2019.
 * hnf Co.Ltd. 2002-
 * All rights resolved
 */
package com.kixs.redis.redis;

import com.kixs.redis.RedisApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * TODO File Description
 *
 * @author wangbing
 * @version v1.0.0
 * @date 2019/8/16 18:20
 */
@Slf4j
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("")
    public String add(@RequestParam("key") String key, @RequestParam("value") String value) {
        stringRedisTemplate.opsForValue().set(key, value);

        return RedisApplication.SUCCESS;
    }

    @DeleteMapping("")
    public String del(@RequestParam("key") String key) {
        stringRedisTemplate.delete(key);
        return RedisApplication.SUCCESS;
    }

    @GetMapping("")
    public String get(@RequestParam("key") String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }
}
