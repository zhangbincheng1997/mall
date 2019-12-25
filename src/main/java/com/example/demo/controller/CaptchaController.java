package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.component.RedisService;
import com.example.demo.utils.Constants;
import com.wf.captcha.GifCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 验证码 https://github.com/whvcse/EasyCaptcha
 */
@Controller
public class CaptchaController {

    @Autowired
    private RedisService redisService;

    @GetMapping(value = "/captcha")
    @ResponseBody
    public Result captcha() throws Exception {
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
