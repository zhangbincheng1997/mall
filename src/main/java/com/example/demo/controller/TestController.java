package com.example.demo.controller;

import com.example.demo.component.RedisLocker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Api(tags = "测试")
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RedisLocker redisLocker;

    private static int counter = 0;

    @ApiOperation("测试加锁")
    @GetMapping(value = "/lock")
    @ResponseBody
    public void lock() {
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        redisLocker.lock("test", 1L);
                        log.info(String.valueOf(++counter));
                    } finally {
                        redisLocker.unlock("test");
                    }
                }
            }).start();
        }
    }
}
