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

    @ApiOperation("购买 PC端支付 下单")// TODO 高并发 防止超卖
    @PostMapping("")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')") // RequestBody JSON
    public void buy(@ApiIgnore Principal principal, @Valid @RequestBody OrderMasterDto orderMasterDto,
                    HttpServletResponse response) {
        // 创建订单
        OrderMaster order = orderService.buy(principal.getName(), orderMasterDto);
        // 调用接口
        payService.pay(order.getId(), order.getAmount(), response);
    }

    @ApiOperation("购买 PC端支付 下单但是之前未付款")
    @PostMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')") // RequestBody JSON
    public void buyById(@ApiIgnore Principal principal, @PathVariable("id") Long id, HttpServletResponse response) {
        // 查找订单
        OrderMaster order = orderService.getByBuyer(principal.getName(), id);
        // 调用接口
        payService.pay(order.getId(), order.getAmount(), response);
    }

    @ApiOperation("买家关闭订单")
    @PostMapping(value = "/close/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public Result<String> close(@ApiIgnore Principal principal, @PathVariable("id") Long id) {
        // 确认存在
        OrderMaster order = orderService.getByBuyer(principal.getName(), id);
        // 检查状态
        if (!order.getOrderStatus().equals(OrderStatusEnum.NEW.getCode()))
            return Result.failure(Status.ORDER_NOT_NEW); // 不属于新订单
        if (!order.getPayStatus().equals(PayStatusEnum.FALSE.getCode()))
            return Result.failure(Status.ORDER_PAY); // 不属于未付款
        // 处理关闭
        payService.close(order.getId());
        // 不管是否关闭成功，都更新状态，不同于其他方法
        orderService.returnStock(id);
        orderService.updateOrderStatus(id, OrderStatusEnum.CLOSED.getCode());
        return Result.success();
    }

    @ApiOperation("买家申请退款")
    @PostMapping(value = "/refund/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public Result<String> refund(@ApiIgnore Principal principal, @PathVariable("id") Long id) {
        // 确认存在
        OrderMaster order = orderService.getByBuyer(principal.getName(), id);
        // 检查状态
        if (!order.getOrderStatus().equals(OrderStatusEnum.NEW.getCode()))
            return Result.failure(Status.ORDER_NOT_NEW); // 不属于新订单
        if (!order.getPayStatus().equals(PayStatusEnum.TRUE.getCode()))
            return Result.failure(Status.ORDER_NOT_PAY); // 不属于已付款
        // 修改订单状态
        int count = orderService.updateOrderStatus(id, OrderStatusEnum.BUYER_REQUEST_REFUND.getCode());
        if (count != 0) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }
}