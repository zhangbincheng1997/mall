package com.example.demo.controller;

import cn.hutool.core.convert.Convert;
import com.example.demo.base.PageResult;
import com.example.demo.base.Result;
import com.example.demo.dto.page.OrderPageRequest;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.OrderMaster;
import com.example.demo.service.BuyerOrderService;
import com.example.demo.vo.OrderDetailVo;
import com.example.demo.vo.OrderMasterVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "买家订单")
@Controller
@RequestMapping("/buyer/order")
public class BuyerOrderController {

    @Autowired
    private BuyerOrderService buyerOrderService;

    @ApiOperation("获取订单列表")
    @GetMapping("/list")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public PageResult<List<OrderMasterVo>> list(@ApiIgnore Principal principal, @Valid OrderPageRequest pageRequest) {
        // page
        PageInfo<OrderMaster> pageInfo = buyerOrderService.list(principal.getName(), pageRequest);
        // convert
        List<OrderMasterVo> orderMasterVoList = pageInfo.getList()
                .stream()
                .map(order -> Convert.convert(OrderMasterVo.class, order))
                .collect(Collectors.toList());
        return PageResult.success(orderMasterVoList, pageInfo.getTotal());
    }

    @ApiOperation("获取订单详情")
    @GetMapping("/detail/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public Result<List<OrderDetailVo>> detail(@ApiIgnore Principal principal, @PathVariable("id") Long id) {
        List<OrderDetail> orderDetailList = buyerOrderService.getDetail(principal.getName(), id);
        List<OrderDetailVo> orderDetailVoList = orderDetailList
                .stream()
                .map(orderDetail -> Convert.convert(OrderDetailVo.class, orderDetail))
                .collect(Collectors.toList());
        return Result.success(orderDetailVoList);
    }
}
