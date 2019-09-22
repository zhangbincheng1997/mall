package com.example.demo.controller;

import com.example.demo.access.AccessLimit;
import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.RabbitSender;
import com.example.demo.component.RedisService;
import com.example.demo.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Controller
@Validated
public class EmailController {

    @Autowired
    private RabbitSender rabbitSender;

    @Autowired
    private RedisService redisService;

    /**
     * 发送邮箱验证码
     * 限制每分钟请求一次
     *
     * @param to
     * @return
     */
    @RequestMapping("sendEmail")
    @ResponseBody
    @AccessLimit(seconds = 60, maxCount = 1, needLogin = false)
    public Result sendEmail(@RequestParam @Email String to) {
        rabbitSender.send(Constants.EMAIL_TOPIC, to);
        return Result.success("");
    }

    /**
     * 验证邮箱验证码
     * 限制每分钟请求三次
     *
     * @param to
     * @param code
     * @return
     */
    @RequestMapping("checkEmail")
    @ResponseBody
    @AccessLimit(seconds = 60, maxCount = 3, needLogin = false)
    public Result checkEmail(@RequestParam @Email String to,
                             @RequestParam @Size(min = 4, max = 4) String code) {
        String redisCode = (String) redisService.get(Constants.EMAIL_KEY + "_" + to);
        if (redisCode == null) {
            return Result.error(Status.CODE_EXPIRED);
        } else if (!redisCode.equals(code)) {
            return Result.error(Status.CODE_ERROR);
        } else {
            return Result.success("");
        }
    }
}
