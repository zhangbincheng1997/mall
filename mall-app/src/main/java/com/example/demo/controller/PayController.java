package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.facade.PayFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Api(tags = "支付")
@RestController
@RequestMapping("/pay")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
public class PayController {

    @Autowired
    private PayFacade payFacade;

    @ApiOperation("订单支付")
    @PostMapping("/{id}")
    public void pay(@ApiIgnore Principal principal, @PathVariable("id") Long id, HttpServletResponse response) {
        payFacade.pay(principal.getName(), id, response);
    }

    @ApiOperation("订单关闭")
    @PostMapping(value = "/close/{id}")
    public Result<String> close(@ApiIgnore Principal principal, @PathVariable("id") Long id) {
        payFacade.close(principal.getName(), id);
        return Result.success();
    }

    @ApiOperation("订单完成")
    @PostMapping(value = "/receive/{id}")
    public Result<String> receive(@ApiIgnore Principal principal, @PathVariable("id") Long id) {
        payFacade.receive(principal.getName(), id);
        return Result.success();
    }

    @ApiOperation("订单申请退款")
    @PostMapping(value = "/refund/{id}")
    public Result<String> refund(@ApiIgnore Principal principal, @PathVariable("id") Long id) {
        payFacade.refund(principal.getName(), id);
        return Result.success();
    }
}