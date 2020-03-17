package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.pay.PayService;
import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.entity.OrderMaster;
import com.example.demo.service.OrderMasterService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderMasterService orderMasterService;

    @ApiOperation("卖家处理买家退款")
    @PostMapping(value = "/deal/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    // 防止重复处理退款
    public Result<String> deal(@PathVariable("id") Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterService.get(id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.REFUND_REQUEST.getCode()))
            return Result.failure(Status.ORDER_NOT_REFUND_REQUEST);
        // 处理退款
        boolean isSuccess = payService.refund(orderMaster.getId(), orderMaster.getAmount());
        if (isSuccess) { // 退款成功，更新状态
            // 修改商品数量
            orderMasterService.increaseStock(id); // MYSQL
            orderMasterService.addStockRedis(id); // REDIS
            orderMasterService.updateOrderStatus(id, OrderStatusEnum.REFUND_SUCCESS.getCode());
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("卖家取消订单 太久未支付")
    @PostMapping(value = "/cancel/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    // 防止重复关闭订单
    public Result<String> cancel(@PathVariable("id") Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterService.get(id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_PAID.getCode()))
            return Result.failure(Status.ORDER_NOT_TO_BE_PAID);
        // 不管是否关闭成功，都更新状态，不同于其他方法
        orderMasterService.addStockRedis(id);
        orderMasterService.updateOrderStatus(id, OrderStatusEnum.CANCEL.getCode());
        return Result.success();
    }

    @ApiOperation("卖家订单发货")
    @PostMapping(value = "/ship/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    // 防止重复完成订单
    public Result<String> ship(@PathVariable("id") Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterService.get(id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_SHIPPED.getCode())) // 待发货
            return Result.failure(Status.ORDER_NOT_TO_BE_SHIPPED);
        // 修改订单状态
        orderMasterService.updateOrderStatus(id, OrderStatusEnum.TO_BE_RECEIVED.getCode());
        return Result.success();
    }
}