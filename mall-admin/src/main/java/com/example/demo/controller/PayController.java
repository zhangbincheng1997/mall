package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.pay.PayService;
import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.entity.OrderMaster;
import com.example.demo.facade.OrderMasterFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "支付")
@RestController
@RequestMapping("/pay")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
public class PayController {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderMasterFacade orderMasterFacade;

    @ApiOperation("订单处理退款")
    @PostMapping(value = "/deal/{id}")
    public Result<String> deal(@PathVariable("id") Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterFacade.get(id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.REFUND_REQUEST.getCode())) // 退款请求
            return Result.failure(Status.ORDER_NOT_REFUND_REQUEST);
        // 订单处理退款
        boolean isSuccess = payService.refund(orderMaster.getId(), orderMaster.getAmount());
        if (isSuccess) {
            orderMasterFacade.addStockMySQL(id); // MYSQL
            orderMasterFacade.addStockRedis(id); // REDIS
            orderMasterFacade.updateOrderStatus(id, OrderStatusEnum.REFUND_SUCCESS.getCode());
            return Result.success();
        }
        return Result.failure();
    }

    @ApiOperation("订单取消")
    @PostMapping(value = "/cancel/{id}")
    public Result<String> cancel(@PathVariable("id") Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterFacade.get(id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_PAID.getCode())) // 待付款
            return Result.failure(Status.ORDER_NOT_TO_BE_PAID);
        // 订单取消
        orderMasterFacade.addStockMySQL(id); // MYSQL
        orderMasterFacade.addStockRedis(id); // REDIS
        orderMasterFacade.updateOrderStatus(id, OrderStatusEnum.CANCEL.getCode());
        return Result.success();
    }

    @ApiOperation("订单发货")
    @PostMapping(value = "/ship/{id}")
    public Result<String> ship(@PathVariable("id") Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterFacade.get(id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_SHIPPED.getCode())) // 待发货
            return Result.failure(Status.ORDER_NOT_TO_BE_SHIPPED);
        // 订单发货
        orderMasterFacade.updateOrderStatus(id, OrderStatusEnum.TO_BE_RECEIVED.getCode());
        return Result.success();
    }
}