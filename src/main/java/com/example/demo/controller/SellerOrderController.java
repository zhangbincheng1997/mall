package com.example.demo.controller;

import com.example.demo.base.PageResult;
import com.example.demo.base.Result;
import com.example.demo.dto.PageRequest;
import com.example.demo.service.OrderService;
import com.example.demo.vo.OrderMasterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "商家订单")
@Controller
@RequestMapping("/seller/order")
public class SellerOrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("获取订单")
    @GetMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result get(@PathVariable("id") Long id) {
        OrderMasterVo orderMasterVo = orderService.get(id);
        return Result.success(orderMasterVo);
    }

    @ApiOperation("获取订单列表")
    @GetMapping("/list")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PageResult list(@Valid PageRequest pageRequest) {
        PageResult pageResult = orderService.list(pageRequest);
        return pageResult;
    }

    @ApiOperation("修改订单状态")
    @PutMapping("/{id}/status")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result updateOrderStatus(@PathVariable("id") Long id,
                                    @RequestParam(name = "orderStatus", defaultValue = "0") Integer orderStatus) {
        int count = orderService.updateOrderStatus(id, orderStatus);
        if (count == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }
}
