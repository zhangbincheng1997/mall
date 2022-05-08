package cn.jnu.common.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.GifCaptcha;
import cn.jnu.common.base.Result;
import cn.jnu.common.component.redis.RedisService;
import cn.jnu.common.utils.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Api(tags = "验证码")
@RestController
@RequestMapping("/captcha")
@AllArgsConstructor
public class CaptchaController {

    private RedisService redisService;

    @ApiOperation("获取验证码")
    @GetMapping("")
    public Result<Map<String, String>> captcha() {
        GifCaptcha captcha = CaptchaUtil.createGifCaptcha(Constants.CAPTCHA_WIDTH, Constants.CAPTCHA_HEIGHT, Constants.CAPTCHA_LENGTH);
        String key = UUID.randomUUID().toString().replace("-", "");
        String code = captcha.getCode().toLowerCase();
        redisService.set(Constants.CAPTCHA_KEY + key, code, Constants.CAPTCHA_EXPIRE);
        Map<String, String> result = new HashMap<>();
        result.put("key", key);
        result.put("image", captcha.getImageBase64Data());
        return Result.success(result);
    }
}
