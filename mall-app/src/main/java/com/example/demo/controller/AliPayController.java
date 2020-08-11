package com.example.demo.controller;

import com.example.demo.component.pay.PayService;
import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.facade.OrderMasterFacade;
import com.example.demo.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class AliPayController {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderMasterFacade orderMasterFacade;

    @GetMapping(value = "/return") // 返回给客户端
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
    public String Notify(HttpServletRequest request) {
        boolean verifyResult = payService.check(request);
        if (verifyResult) {
            // https://docs.open.alipay.com/270/105899/
            Long id = new Long(request.getParameter("out_trade_no")); // id
            orderMasterFacade.updateOrderStatus(id, OrderStatusEnum.TO_BE_SHIPPED.getCode());
            log.info("Notify 验证成功");
            return "success";
        }
        log.info("Notify 验证失败");
        return "failure";
    }
}