package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.pay.PayService;
import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.facade.OrderMasterFacade;
import com.example.demo.jwt.JwtUserDetails;
import com.example.demo.entity.OrderMaster;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Slf4j
@Api(tags = "支付")
@RestController
@RequestMapping("/pay")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
public class PayController {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderMasterFacade orderMasterFacade;

    @ApiOperation("购买 创建订单")
    @PostMapping("")
    public Result<String> create(@ApiIgnore Authentication authentication) {
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        String orderId = orderMasterFacade.buy(userDetails.getUser());
        return Result.success(orderId);
    }

    @ApiOperation("购买 PC端支付")
    @PostMapping("/{id}")
    public void buy(@ApiIgnore Principal principal, @PathVariable("id") Long id, HttpServletResponse response) {
        OrderMaster orderMaster = orderMasterFacade.get(principal.getName(), id);
        payService.pay(orderMaster.getId(), orderMaster.getAmount(), response);
    }

    @ApiOperation("订单取消")
    @PostMapping(value = "/cancel/{id}")
    public Result<String> cancel(@ApiIgnore Principal principal, @PathVariable("id") Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterFacade.get(principal.getName(), id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_PAID.getCode())) // 待付款
            return Result.failure(Status.ORDER_NOT_TO_BE_PAID);
        // 订单取消
        orderMasterFacade.addStockMySQL(id); // MYSQL
        orderMasterFacade.addStockRedis(id); // REDIS
        orderMasterFacade.updateOrderStatus(id, OrderStatusEnum.CANCEL.getCode());
        return Result.success();
    }

    @ApiOperation("订单完成")
    @PostMapping(value = "/receive/{id}")
    public Result<String> receive(@ApiIgnore Principal principal, @PathVariable("id") Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterFacade.get(principal.getName(), id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_RECEIVED.getCode())) // 待收货
            return Result.failure(Status.ORDER_NOT_TO_BE_RECEIVED);
        // 订单完成
        orderMasterFacade.updateOrderStatus(id, OrderStatusEnum.FINISH.getCode());
        return Result.success();
    }

    @ApiOperation("订单申请退款")
    @PostMapping(value = "/refund/{id}")
    public Result<String> refund(@ApiIgnore Principal principal, @PathVariable("id") Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterFacade.get(principal.getName(), id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_SHIPPED.getCode())) // 待发货
            return Result.failure(Status.ORDER_NOT_TO_BE_SHIPPED);
        // 订单申请退款
        orderMasterFacade.updateOrderStatus(id, OrderStatusEnum.REFUND_REQUEST.getCode());
        return Result.success();
    }
}