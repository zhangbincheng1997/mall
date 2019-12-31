package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.PayService;
import com.example.demo.dto.OrderMasterDto;
import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.enums.PayStatusEnum;
import com.example.demo.model.OrderMaster;
import com.example.demo.service.OrderService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/buyer/pay")
public class BuyerPayController {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderService orderService;

    @ApiOperation("购买 PC端支付")
    @PostMapping("")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public void buy(Principal principal, @Valid @RequestBody OrderMasterDto orderMasterDto,
                    HttpServletResponse response) {
        // 创建订单
        OrderMaster order = orderService.buy(principal.getName(), orderMasterDto);
        // 调用接口
        payService.pay(order.getId(), order.getAmount(), response);
    }

    @ApiOperation("买家申请退款")
    @RequestMapping(value = "/refund/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public Result<String> refund(@ApiIgnore Principal principal, @PathVariable("id") Long id) throws Exception {
        // 确认存在
        OrderMaster order = orderService.getByBuyer(principal.getName(), id);
        // 检查状态
        if (order.getOrderStatus().equals(OrderStatusEnum.NEW.getCode()))
            return Result.failure(Status.ORDER_NOT_NEW); // 不属于新订单
        if (order.getPayStatus().equals(PayStatusEnum.FALSE.getCode()))
            return Result.failure(Status.ORDER_NOT_PAY); // 未付款
        // 修改订单状态
        int count1 = orderService.updateOrderStatus(id, OrderStatusEnum.BUYER_REQUEST_REFUND.getCode());
        int count2 = orderService.updatePayStatus(id, PayStatusEnum.REFUND.getCode());
        if (count1 != 0 && count2 != 0) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }
}