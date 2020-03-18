package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.component.redis.RedisService;
import com.example.demo.utils.Constants;
import com.wf.captcha.GifCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Api(tags = "验证码")
@Controller
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private RedisService redisService;

    @ApiOperation("获取验证码")
    @GetMapping("")
    @ResponseBody
    public Result<Map<String, String>> captcha() {
        // 使用静态验证码
        // SpecCaptcha specCaptcha = new SpecCaptcha(Constants.CODE_WIDTH, Constants.CODE_HEIGHT, Constants.CODE_LENGTH);
        // 使用动态验证码
        GifCaptcha gifCaptcha = new GifCaptcha(Constants.CODE_WIDTH, Constants.CODE_HEIGHT, Constants.CODE_LENGTH);
        String key = UUID.randomUUID().toString();
        String code = gifCaptcha.text().toLowerCase();
        redisService.set(key, code, Constants.CODE_EXPIRE);
        Map<String, String> result = new HashMap<>();
        result.put("key", key);
        result.put("image", gifCaptcha.toBase64());
        return Result.success(result);
    }
}
