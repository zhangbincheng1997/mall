package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.pay.PayService;
import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.entity.OrderMaster;
import com.example.demo.service.OrderMasterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(tags = "支付")
@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderMasterService orderMasterService;

    @ApiOperation("订单处理退款")
    @PostMapping(value = "/deal/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Result<String> deal(@PathVariable("id") Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterService.get(id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.REFUND_REQUEST.getCode())) // 退款请求
            return Result.failure(Status.ORDER_NOT_REFUND_REQUEST);
        // 订单处理退款
        boolean isSuccess = payService.refund(orderMaster.getId(), orderMaster.getAmount());
        if (isSuccess) {
            orderMasterService.addStockMySQL(id); // MYSQL
            orderMasterService.addStockRedis(id); // REDIS
            orderMasterService.updateOrderStatus(id, OrderStatusEnum.REFUND_SUCCESS.getCode());
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("订单取消")
    @PostMapping(value = "/cancel/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Result<String> cancel(@PathVariable("id") Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterService.get(id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_PAID.getCode())) // 待付款
            return Result.failure(Status.ORDER_NOT_TO_BE_PAID);
        // 订单取消
        orderMasterService.addStockRedis(id); // REDIS
        orderMasterService.updateOrderStatus(id, OrderStatusEnum.CANCEL.getCode());
        return Result.success();
    }

    @ApiOperation("订单发货")
    @PostMapping(value = "/ship/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Result<String> ship(@PathVariable("id") Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterService.get(id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_SHIPPED.getCode())) // 待发货
            return Result.failure(Status.ORDER_NOT_TO_BE_SHIPPED);
        // 订单发货
        orderMasterService.updateOrderStatus(id, OrderStatusEnum.TO_BE_RECEIVED.getCode());
        return Result.success();
    }
}