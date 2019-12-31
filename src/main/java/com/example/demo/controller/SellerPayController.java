package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.PayService;
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

@Slf4j
@Controller
@RequestMapping("/seller/pay")
public class SellerPayController {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderService orderService;

    @ApiOperation("卖家主动退款")
    @RequestMapping(value = "/refund/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Result<String> refund(@PathVariable("id") Long id) {
        // 确认存在
        OrderMaster order = orderService.get(id);
        // 检查状态
        if (!order.getOrderStatus().equals(OrderStatusEnum.NEW.getCode()))
            return Result.failure(Status.ORDER_NOT_NEW); // 不属于新订单
        if (!order.getPayStatus().equals(PayStatusEnum.TRUE.getCode()))
            return Result.failure(Status.ORDER_NOT_PAY); // 未付款
        // 处理退款
        boolean isSuccess = payService.refund(order.getId(), order.getAmount());
        if (isSuccess) { // 退款成功，更新状态
            orderService.updateOrderStatus(id, OrderStatusEnum.SELLER_REFUND_SUCCESS.getCode()); // SELLER_REFUND_SUCCESS
            orderService.updatePayStatus(id, PayStatusEnum.REFUND.getCode());
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("卖家处理买家退款")
    @RequestMapping(value = "/deal/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Result<String> deal(@PathVariable("id") Long id) {
        // 确认存在
        OrderMaster order = orderService.get(id);
        // 检查状态
        if (!order.getOrderStatus().equals(OrderStatusEnum.BUYER_REQUEST_REFUND.getCode()))
            return Result.failure(Status.ORDER_NOT_REQUEST_REFUND); // 订单没有申请退款
        // 处理退款
        boolean isSuccess = payService.refund(order.getId(), order.getAmount());
        if (isSuccess) { // 退款成功，更新状态
            orderService.updateOrderStatus(id, OrderStatusEnum.BUYER_REFUND_SUCCESS.getCode()); // BUYER_REFUND_SUCCESS
            orderService.updatePayStatus(id, PayStatusEnum.REFUND.getCode());
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("卖家完成订单")
    @RequestMapping(value = "/finish/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Result<String> finish(@PathVariable("id") Long id) {
        // 确认存在
        OrderMaster order = orderService.get(id);
        // 检查状态
        if (!order.getOrderStatus().equals(OrderStatusEnum.NEW.getCode()))
            return Result.failure(Status.ORDER_NOT_NEW); // 不属于新订单
        if (!order.getPayStatus().equals(PayStatusEnum.TRUE.getCode()))
            return Result.failure(Status.ORDER_NOT_PAY); // 未付款
        // 修改订单状态
        int count = orderService.updateOrderStatus(id, OrderStatusEnum.FINISH.getCode());
        if (count != 0) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }
}