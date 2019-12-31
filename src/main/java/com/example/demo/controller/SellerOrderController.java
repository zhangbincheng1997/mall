package com.example.demo.controller;

import cn.hutool.core.convert.Convert;
import com.example.demo.base.PageResult;
import com.example.demo.base.Result;
import com.example.demo.dto.PageRequest;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.OrderMaster;
import com.example.demo.service.OrderService;
import com.example.demo.vo.OrderDetailVo;
import com.example.demo.vo.OrderMasterVo;
import com.example.demo.vo.ProductVo;
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
    private OrderService orderService;

    @ApiOperation("获取订单")
    @GetMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result<OrderMasterVo> get(@PathVariable("id") Long id) {
        // get
        OrderMaster orderMaster = orderService.get(id);
        // convert
        OrderMasterVo orderMasterVo = Convert.convert(OrderMasterVo.class, orderMaster);
        // add detail
        orderMasterVo.setProducts(getDetail(orderMaster.getId()));
        return Result.success(orderMasterVo);
    }

    @ApiOperation("获取订单列表")
    @GetMapping("/list")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PageResult<List<OrderMasterVo>> list(@Valid PageRequest pageRequest) {
        // page
        PageInfo<OrderMaster> pageInfo = orderService.list(pageRequest);
        // convert
        List<OrderMasterVo> orderMasterVoList = pageInfo.getList()
                .stream()
                .map(order -> Convert.convert(OrderMasterVo.class, order))
                .collect(Collectors.toList());
        // add detail
        orderMasterVoList.forEach(orderMasterVo -> orderMasterVo.setProducts(getDetail(orderMasterVo.getId())));
        return PageResult.success(orderMasterVoList, pageInfo.getTotal());
    }

    @ApiOperation("修改订单状态")
    @PutMapping("/{id}/status")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result<String> updateOrderStatus(@PathVariable("id") Long id,
                                            @RequestParam(name = "orderStatus", defaultValue = "0") Integer orderStatus) {
        int count = orderService.updateOrderStatus(id, orderStatus);
        if (count == 1) {
            return Result.success();
        } else {
            return Result.failure();
        }
    }

    private List<OrderDetailVo> getDetail(Long id) {
        List<OrderDetail> orderDetailList = orderService.getDetail(id);
        return orderDetailList
                .stream()
                .map(orderDetail -> Convert.convert(OrderDetailVo.class, orderDetail))
                .collect(Collectors.toList());
    }
}
