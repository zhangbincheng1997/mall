package com.example.demo.controller;

import com.example.demo.component.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class PayController {

    @Autowired
    private PayService payService;

    @RequestMapping(value = "/return")
    @ResponseBody
    public String Return(HttpServletRequest request)  {
        boolean verifyResult = payService.check(request);
        if (verifyResult) {
            log.info("Return 验证成功");
            return "success";
        } else {
            log.info("Return 验证失败");
            return "failure";
        }
    }

    @RequestMapping(value = "/notify")
    @ResponseBody
    public String Notify(HttpServletRequest request)  {
        boolean verifyResult = payService.check(request);
        if (verifyResult) {
            // TODO 更新状态 取出orderId参数
            log.info("Notify 验证成功");
            return "success";
        } else {
            log.info("Notify 验证失败");
            return "failure";
        }
    }
}