package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.facade.PayFacade;
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
    private PayFacade payFacade;

    @ApiOperation("订单处理退款")
    @PostMapping(value = "/deal/{id}")
    public Result<String> deal(@PathVariable("id") Long id) {
        payFacade.deal(id);
        return Result.success();
    }

    @ApiOperation("订单关闭")
    @PostMapping(value = "/close/{id}")
    public Result<String> close(@PathVariable("id") Long id) {
        payFacade.close(id);
        return Result.success();
    }

    @ApiOperation("订单发货")
    @PostMapping(value = "/ship/{id}")
    public Result<String> ship(@PathVariable("id") Long id) {
        payFacade.ship(id);
        return Result.success();
    }
}