package com.example.demo.controller;

import com.example.demo.aop.AccessLimit;
import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.PayService;
import com.example.demo.dto.OrderMasterDto;
import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.enums.PayStatusEnum;
import com.example.demo.model.OrderMaster;
import com.example.demo.service.OrderService;
import com.example.demo.vo.OrderMasterVo;
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

    @ApiOperation("购买 创建订单")
    @PostMapping("")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')") // RequestBody JSON
    @AccessLimit(ip = true, time = 10, count = 1) // 防止重复下单
    public Result<String> buy(@ApiIgnore Principal principal, @Valid @RequestBody OrderMasterDto orderMasterDto,
                    HttpServletResponse response) {
        // 创建订单
        orderService.buy(principal.getName(), orderMasterDto);
        return Result.success();
    }

    @ApiOperation("购买 创建订单")
    @GetMapping("/polling/{uuid}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')") // RequestBody JSON
    public Result<OrderMasterVo> buy(String uuid) {
        // 创建订单
        OrderMasterVo orderMasterVo = orderService.polling(uuid);
        if(orderMasterVo !=null) { // 成功
            return Result.success(orderMasterVo);
        }else {
            return Result.failure();
        }
    }

    @ApiOperation("购买 PC端支付")
    @PostMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')") // RequestBody JSON
    @AccessLimit(ip = true, time = 10, count = 1) // 防止重复下单
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
    @AccessLimit(ip = true, time = 10, count = 1) // 防止重复关闭订单
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
        orderService.addStockRedis(id);
        orderService.updateOrderStatus(id, OrderStatusEnum.CLOSED.getCode());
        return Result.success();
    }

    @ApiOperation("买家申请退款")
    @PostMapping(value = "/refund/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @AccessLimit(ip = true, time = 10, count = 1) // 防止重复申请退款
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