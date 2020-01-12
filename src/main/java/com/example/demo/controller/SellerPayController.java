package com.example.demo.controller;

import com.example.demo.aop.AccessLimit;
import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.PayService;
import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.model.OrderMaster;
import com.example.demo.service.OrderService;
import com.example.demo.service.SellerOrderService;
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
    private SellerOrderService sellerOrderService;

    @Autowired
    private OrderService orderService;

    @ApiOperation("卖家处理买家退款")
    @PostMapping(value = "/deal/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @AccessLimit(ip = true, time = 1, count = 1) // 防止重复处理退款
    public Result<String> deal(@PathVariable("id") Long id) {
        // 确认存在
        OrderMaster order = sellerOrderService.get(id);
        // 检查状态
        if (!order.getOrderStatus().equals(OrderStatusEnum.REFUND_REQUEST.getCode()))
            return Result.failure(Status.ORDER_NOT_REFUND_REQUEST);
        // 处理退款
        boolean isSuccess = payService.refund(order.getId(), order.getAmount());
        if (isSuccess) { // 退款成功，更新状态
            // 修改商品数量
            orderService.increaseStock(id); // MYSQL
            orderService.addStockRedis(id); // REDIS
            orderService.updateOrderStatus(id, OrderStatusEnum.REFUND_SUCCESS.getCode());
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("卖家取消订单 太久未支付")
    @PostMapping(value = "/cancel/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @AccessLimit(ip = true, time = 1, count = 1) // 防止重复关闭订单
    public Result<String> cancel(@PathVariable("id") Long id) {
        // 确认存在
        OrderMaster order = sellerOrderService.get(id);
        // 检查状态
        if (!order.getOrderStatus().equals(OrderStatusEnum.TO_BE_PAID.getCode()))
            return Result.failure(Status.ORDER_NOT_TO_BE_PAID);
        // 不管是否关闭成功，都更新状态，不同于其他方法
        orderService.addStockRedis(id);
        orderService.updateOrderStatus(id, OrderStatusEnum.CANCEL.getCode());
        return Result.success();
    }

    @ApiOperation("卖家订单发货")
    @PostMapping(value = "/ship/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @AccessLimit(ip = true, time = 1, count = 1) // 防止重复完成订单
    public Result<String> ship(@PathVariable("id") Long id) {
        // 确认存在
        OrderMaster order = sellerOrderService.get(id);
        // 检查状态
        if (!order.getOrderStatus().equals(OrderStatusEnum.TO_BE_SHIPPED.getCode())) // 待发货
            return Result.failure(Status.ORDER_NOT_TO_BE_SHIPPED);
        // 修改订单状态
        orderService.updateOrderStatus(id, OrderStatusEnum.TO_BE_RECEIVED.getCode());
        return Result.success();
    }
}