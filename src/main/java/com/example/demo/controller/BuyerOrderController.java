package com.example.demo.controller;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.base.PageResult;
import com.example.demo.base.Result;
import com.example.demo.dto.OrderMasterDto;
import com.example.demo.dto.PageRequest;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.OrderMaster;
import com.example.demo.service.OrderService;
import com.example.demo.vo.OrderDetailVo;
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
import java.util.stream.Collectors;

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
        // get
        OrderMaster orderMaster = orderService.getByBuyer(principal.getName(), id);
        // convert
        OrderMasterVo orderMasterVo = getDetail(orderMaster);
        return Result.success(orderMasterVo);
    }

    @ApiOperation("获取订单列表")
    @GetMapping("/list")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public PageResult<List<OrderMasterVo>> list(Principal principal, @Valid PageRequest pageRequest) {
        // page
        PageInfo<OrderMaster> pageInfo = orderService.listByBuyer(principal.getName(), pageRequest);
        // convert
        List<OrderMasterVo> orderMasterVoList = pageInfo.getList()
                .stream()
                .map(order -> getDetail(order))
                .collect(Collectors.toList());
        return PageResult.success(orderMasterVoList, pageInfo.getTotal());
    }

    // 重复了...待优化
    private OrderMasterVo getDetail(OrderMaster orderMaster) {
        OrderMasterVo orderMasterVo = Convert.convert(OrderMasterVo.class, orderMaster);
        List<OrderDetail> orderDetailList = orderService.getDetail(orderMaster.getId());
        List<OrderDetailVo> orderDetailVoList = orderDetailList
                .stream()
                .map(orderDetail -> Convert.convert(OrderDetailVo.class, orderDetail))
                .collect(Collectors.toList());
        orderMasterVo.setProducts(orderDetailVoList);
        return orderMasterVo;
    }
}
