package com.example.demo.controller;

import cn.hutool.core.convert.Convert;
import com.example.demo.base.PageResult;
import com.example.demo.base.Result;
import com.example.demo.dto.PageRequest;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.OrderMaster;
import com.example.demo.service.OrderService;
import com.example.demo.service.SellerOrderService;
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
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "商家订单")
@Controller
@RequestMapping("/seller/order")
public class SellerOrderController {

    @Autowired
    private SellerOrderService sellerOrderService;
    @Autowired
    private OrderService orderService;

    @ApiOperation("获取订单")
    @GetMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result<OrderMasterVo> get(@PathVariable("id") Long id) {
        // get
        OrderMaster orderMaster = sellerOrderService.get(id);
        // convert
        OrderMasterVo orderMasterVo = getDetail(orderMaster);
        return Result.success(orderMasterVo);
    }

    @ApiOperation("获取订单列表")
    @GetMapping("/list")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PageResult<List<OrderMasterVo>> list(@Valid PageRequest pageRequest) {
        // page
        PageInfo<OrderMaster> pageInfo = sellerOrderService.list(pageRequest);
        // convert
        List<OrderMasterVo> orderMasterVoList = pageInfo.getList()
                .stream()
                .map(order -> getDetail(order))
                .collect(Collectors.toList());
        return PageResult.success(orderMasterVoList, pageInfo.getTotal());
    }

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
