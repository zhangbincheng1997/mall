package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.pay.PayService;
import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.jwt.JwtUserDetails;
import com.example.demo.entity.OrderMaster;
import com.example.demo.service.OrderMasterService;
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
@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private PayService payService;

    @Autowired
    private OrderMasterService orderMasterService;

    @ApiOperation("购买 创建订单")
    @PostMapping("")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')") // RequestBody JSON
    // 防止重复下单
    public Result<String> create(@ApiIgnore Authentication authentication) {
        // 创建订单
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        String orderId = orderMasterService.buy(userDetails.getUser());
        return Result.success(orderId);
    }

    @ApiOperation("购买 PC端支付")
    @PostMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    // 防止重复下单
    public void buy(@ApiIgnore Principal principal, @PathVariable("id") Long id, HttpServletResponse response) {
        // 查找订单
        OrderMaster orderMaster = orderMasterService.get(principal.getName(), id);
        // 调用接口
        payService.pay(orderMaster.getId(), orderMaster.getAmount(), response);
    }

    @ApiOperation("买家取消订单")
    @PostMapping(value = "/cancel/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    // 防止重复关闭订单
    public Result<String> close(@ApiIgnore Principal principal, @PathVariable("id") Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterService.get(principal.getName(), id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_PAID.getCode()))
            return Result.failure(Status.ORDER_NOT_TO_BE_PAID);
        // 修改订单状态
        orderMasterService.addStockRedis(id);
        orderMasterService.updateOrderStatus(id, OrderStatusEnum.CANCEL.getCode());
        return Result.success();
    }

    @ApiOperation("买家订单确认收货")
    @PostMapping(value = "/receive/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    // 防止重复完成订单
    public Result<String> confirm(@ApiIgnore Principal principal, @PathVariable("id") Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterService.get(principal.getName(), id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_RECEIVED.getCode())) // 待收货
            return Result.failure(Status.ORDER_NOT_TO_BE_RECEIVED);
        // 修改订单状态
        orderMasterService.updateOrderStatus(id, OrderStatusEnum.FINISH.getCode());
        return Result.success();
    }

    @ApiOperation("买家申请退款")
    @PostMapping(value = "/refund/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    // 防止重复申请退款
    public Result<String> refund(@ApiIgnore Principal principal, @PathVariable("id") Long id) {
        // 确认存在
        OrderMaster orderMaster = orderMasterService.get(principal.getName(), id);
        // 检查状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_SHIPPED.getCode())) // 待发货
            return Result.failure(Status.ORDER_NOT_TO_BE_SHIPPED);
        // 修改订单状态
        orderMasterService.updateOrderStatus(id, OrderStatusEnum.REFUND_REQUEST.getCode());
        return Result.success();
    }
}