package com.example.demo.controller;

import com.example.demo.component.PayService;
import com.example.demo.common.enums.OrderStatusEnum;
import com.example.demo.service.OrderMasterService;
import com.example.demo.common.utils.Constants;
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

    @Autowired
    private OrderMasterService orderMasterService;

    @GetMapping(value = "/return") // 返回给客户端
    @ResponseBody
    public String Return(HttpServletRequest request) {
        boolean verifyResult = payService.check(request);
        if (verifyResult) {
            log.info("Return 验证成功");
            return Constants.PAY_SUCCESS_RETURN;
        }
        log.info("Return 验证失败");
        return Constants.PAY_FAILURE_RETURN;
    }

    @PostMapping(value = "/notify") // 返回给支付宝
    @ResponseBody
    public String Notify(HttpServletRequest request) {
        boolean verifyResult = payService.check(request);
        if (verifyResult) {
            // https://docs.open.alipay.com/270/105899/
            Long id = new Long(request.getParameter("out_trade_no")); // id
            orderMasterService.decreaseStock(id); // MYSQL 真正写入数据库
            orderMasterService.updateOrderStatus(id, OrderStatusEnum.TO_BE_SHIPPED.getCode());
            log.info("Notify 验证成功");
            return "success";
        }
        log.info("Notify 验证失败");
        return "failure";
    }
}