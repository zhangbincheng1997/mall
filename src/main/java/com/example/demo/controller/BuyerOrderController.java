package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.base.PageResult;
import com.example.demo.base.Result;
import com.example.demo.dto.OrderMasterDto;
import com.example.demo.dto.PageRequest;
import com.example.demo.service.OrderService;
import com.example.demo.vo.OrderMasterVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Api(tags = "买家订单")
@Controller
@RequestMapping("/buyer/order")
public class BuyerOrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("获取订单")
    @GetMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public Result<OrderMasterVo> get(Principal principal, @PathVariable("id") Long id) {
        OrderMasterVo orderMasterVo = orderService.getByBuyer(principal.getName(), id);
        return Result.success(orderMasterVo);
    }

    @ApiOperation("获取订单列表")
    @GetMapping("/list")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public PageResult<List<OrderMasterVo>> list(Principal principal, @Valid PageRequest pageRequest) {
        PageInfo<OrderMasterVo> pageInfo = orderService.listByBuyer(principal.getName(), pageRequest);
        return PageResult.success(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * {
     * "products": [
     * {
     * "id": 2,
     * "quantity": 2
     * },...
     * ]
     * }
     */
    @ApiOperation("购买")
    @PostMapping("")
    @ResponseBody // TODO 以后换个controller  @RequestBody必须
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public Result<JSONObject> buy(Principal principal, @Valid @RequestBody OrderMasterDto orderMasterDto) {
        Long orderId = orderService.buy(principal.getName(), orderMasterDto);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderId", orderId);
        return Result.success(jsonObject);
    }
}
