package com.example.demo.controller;

import com.example.demo.component.PayService;
import com.example.demo.enums.PayStatusEnum;
import com.example.demo.mapper.OrderMasterMapper;
import com.example.demo.service.OrderService;
import com.example.demo.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class PayController {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderService orderService;

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
            // 接收到异步通知并验签通过后，一定要检查通知内容，
            // 包括通知中的 app_id、out_trade_no、total_amount 是否与请求中的一致，
            // 并根据 trade_status 进行后续业务处理。
            Long id = new Long(request.getParameter("out_trade_no")); // id
            orderService.decreaseStock(id); // MYSQL 真正写入数据库
            int count = orderService.updatePayStatus(id, PayStatusEnum.TRUE.getCode());
            if (count != 0) {
                log.info("Notify 验证成功");
                return "success";
            }
        }
        log.info("Notify 验证失败");
        return "failure";
    }
}