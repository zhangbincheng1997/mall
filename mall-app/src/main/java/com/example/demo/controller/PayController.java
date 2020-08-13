package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.facade.PayFacade;
import com.example.demo.jwt.JwtUserDetails;
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
    private PayFacade payFacade;

    @ApiOperation("购买 创建订单")
    @PostMapping("create")
    public Result<String> create(@ApiIgnore Authentication authentication) {
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        String orderId = payFacade.create(userDetails.getUser());
        return Result.success(orderId);
    }

    @ApiOperation("购买 PC端支付")
    @PostMapping("/{id}")
    public void pay(@ApiIgnore Principal principal, @PathVariable("id") Long id, HttpServletResponse response) {
        payFacade.pay(principal.getName(), id, response);
    }

    @ApiOperation("订单取消")
    @PostMapping(value = "/cancel/{id}")
    public Result<String> cancel(@ApiIgnore Principal principal, @PathVariable("id") Long id) {
        payFacade.cancel(principal.getName(), id);
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