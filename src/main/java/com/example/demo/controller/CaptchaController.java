package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.utils.CaptchaUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class CaptchaController {

    /**
     * 生成验证码
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/getCode")
    public void getCode(HttpServletRequest request, HttpServletResponse response) {
        try {
            CaptchaUtils.out(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查验证码
     *
     * @param request
     * @param code
     * @return
     */
    @RequestMapping(value = "/checkCode")
    @ResponseBody
    public Result checkCode(HttpServletRequest request, @RequestParam String code) {
        if (CaptchaUtils.check(request, code)) {
            return Result.success("");
        } else {
            return Result.error(Status.CAPTCHA_ERROR);
        }
    }
}
