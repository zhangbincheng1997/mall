package com.example.demo.controller;

import cn.hutool.core.convert.Convert;
import com.example.demo.aop.AccessLimit;
import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.PayService;
import com.example.demo.dto.OrderMasterDto;
import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.jwt.JwtUserDetails;
import com.example.demo.model.OrderMaster;
import com.example.demo.service.BuyerOrderService;
import com.example.demo.service.OrderService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    private BuyerOrderService buyerOrderService;

    @Autowired
    private OrderService orderService;

    @ApiOperation("购买 创建订单")
    @PostMapping("")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')") // RequestBody JSON
    @AccessLimit(ip = true, time = 1, count = 1) // 防止重复下单
    public Result<String> buy(@ApiIgnore Authentication authentication,
                              @Valid @RequestBody OrderMasterDto orderMasterDto) {
        // 创建订单
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        buyerOrderService.buy(userDetails.getUser(), orderMasterDto);
        return Result.success();
    }

    @ApiOperation("购买 PC端支付")
    @PostMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @AccessLimit(ip = true, time = 1, count = 1) // 防止重复下单
    public void buyById(@ApiIgnore Principal principal, @PathVariable("id") Long id, HttpServletResponse response) {
        // 查找订单
        OrderMaster order = buyerOrderService.get(principal.getName(), id);
        // 调用接口
        payService.pay(order.getId(), order.getAmount(), response);
    }

    @ApiOperation("买家取消订单")
    @PostMapping(value = "/cancel/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @AccessLimit(ip = true, time = 1, count = 1) // 防止重复关闭订单
    public Result<String> close(@ApiIgnore Principal principal, @PathVariable("id") Long id) {
        // 确认存在
        OrderMaster order = buyerOrderService.get(principal.getName(), id);
        // 检查状态
        if (!order.getOrderStatus().equals(OrderStatusEnum.NEW.getCode()))
            return Result.failure(Status.ORDER_NOT_NEW);
        // 修改订单状态
        orderService.addStockRedis(id);
        orderService.updateOrderStatus(id, OrderStatusEnum.CANCEL.getCode());
        return Result.success();
    }

    @ApiOperation("买家订单确认收货")
    @PostMapping(value = "/confirm/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @AccessLimit(ip = true, time = 1, count = 1) // 防止重复完成订单
    public Result<String> confirm(@ApiIgnore Principal principal, @PathVariable("id") Long id) {
        // 确认存在
        OrderMaster order = buyerOrderService.get(principal.getName(), id);
        // 检查状态
        if (!order.getOrderStatus().equals(OrderStatusEnum.SHIP.getCode()))
            return Result.failure(Status.ORDER_NOT_SHIP);
        // 修改订单状态
        orderService.updateOrderStatus(id, OrderStatusEnum.FINISH.getCode());
        return Result.success();
    }

    @ApiOperation("买家申请退款")
    @PostMapping(value = "/refund/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @AccessLimit(ip = true, time = 1, count = 1) // 防止重复申请退款
    public Result<String> refund(@ApiIgnore Principal principal, @PathVariable("id") Long id) {
        // 确认存在
        OrderMaster order = buyerOrderService.get(principal.getName(), id);
        // 检查状态
        if (!order.getOrderStatus().equals(OrderStatusEnum.PAY.getCode()))
            return Result.failure(Status.ORDER_NOT_PAY);
        // 修改订单状态
        orderService.updateOrderStatus(id, OrderStatusEnum.REFUND_REQUEST.getCode());
        return Result.success();
    }
}