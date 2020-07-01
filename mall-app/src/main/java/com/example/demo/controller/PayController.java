package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.pay.PayService;
import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.jwt.JwtUserDetails;
import com.example.demo.entity.OrderMaster;
import com.example.demo.service.OrderMasterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Slf4j
@Api(tags = "支付")
@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderMasterService orderMasterService;

    // Mysql方案
    @ApiOperation("购买 创建订单")
    @PostMapping("")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')") // RequestBody JSON
    public Result<String> create0(@ApiIgnore Authentication authentication) {
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        String orderId = orderMasterService.buy0(userDetails.getUser());
        return Result.success(orderId);
    }

    // Redis方案
    @ApiOperation("购买 创建订单")
    @PostMapping("")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')") // RequestBody JSON
    public Result<String> create(@ApiIgnore Authentication authentication) {
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        String orderId = orderMasterService.buy(userDetails.getUser());
        return Result.success(orderId);
    }

    @ApiOperation("购买 PC端支付")
    @PostMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public void buy(@ApiIgnore Principal principal, @PathVariable("id") Long id, HttpServletResponse response) {
        OrderMaster orderMaster = orderMasterService.get(principal.getName(), id);
        payService.pay(orderMaster.getId(), orderMaster.getAmount(), response);
    }

    @ApiOperation("订单取消")
    @PostMapping(value = "/cancel/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public Result<String> cancel(@ApiIgnore Principal principal, @PathVariable("id") Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterService.get(principal.getName(), id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_PAID.getCode())) // 待付款
            return Result.failure(Status.ORDER_NOT_TO_BE_PAID);
        // 订单取消
        orderMasterService.addStockRedis(id); // REDIS
        orderMasterService.updateOrderStatus(id, OrderStatusEnum.CANCEL.getCode());
        return Result.success();
    }

    @ApiOperation("订单完成")
    @PostMapping(value = "/receive/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public Result<String> receive(@ApiIgnore Principal principal, @PathVariable("id") Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterService.get(principal.getName(), id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_RECEIVED.getCode())) // 待收货
            return Result.failure(Status.ORDER_NOT_TO_BE_RECEIVED);
        // 订单完成
        orderMasterService.updateOrderStatus(id, OrderStatusEnum.FINISH.getCode());
        return Result.success();
    }

    @ApiOperation("订单退款")
    @PostMapping(value = "/refund/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public Result<String> refund(@ApiIgnore Principal principal, @PathVariable("id") Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterService.get(principal.getName(), id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_SHIPPED.getCode())) // 待发货
            return Result.failure(Status.ORDER_NOT_TO_BE_SHIPPED);
        // 订单退款
        orderMasterService.updateOrderStatus(id, OrderStatusEnum.REFUND_REQUEST.getCode());
        return Result.success();
    }
}