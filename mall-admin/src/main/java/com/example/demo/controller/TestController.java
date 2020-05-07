package com.example.demo.controller;

import cn.hutool.core.lang.Snowflake;
import com.example.demo.base.Result;
import com.example.demo.component.redis.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Slf4j
@Api(tags = "测试")
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private Snowflake snowflake;

    private static final String PRODUCT = "product";
    private static final String LUA_SCRIPT =
            "if redis.call('get', KEYS[1]) >= ARGV[1] "
                    + "then "
                    + "return redis.call('decrby', KEYS[1], ARGV[1]) "
                    + "else "
                    + "return -1 "
                    + "end";
    private static final DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(LUA_SCRIPT, Long.class);

    @ApiOperation("初始化")
    @GetMapping(value = "init")
    @ResponseBody
    public Result<String> init(@RequestParam("num") Long num) {
        redisService.set(PRODUCT, num);
        return Result.success();
    }

    @ApiOperation("购买")
    @GetMapping(value = "buy")
    @ResponseBody
    public Result<Long> buy() {
        Long result = redisService.execute(redisScript, Collections.singletonList(PRODUCT), 1);
        if (result == -1) {
            return Result.failure();
        }
        return Result.success(snowflake.nextId());
    }

    @ApiOperation("结果")
    @GetMapping(value = "result")
    @ResponseBody
    public Result<Integer> result() {
        return Result.success((Integer) redisService.get(PRODUCT));
    }
}
