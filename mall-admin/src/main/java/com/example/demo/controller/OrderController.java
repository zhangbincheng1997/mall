package com.example.demo.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.base.PageResult;
import com.example.demo.base.Result;
import com.example.demo.dto.page.OrderPageRequest;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.OrderMaster;
import com.example.demo.entity.OrderTimeline;
import com.example.demo.facade.OrderMasterFacade;
import com.example.demo.vo.OrderDetailVo;
import com.example.demo.vo.OrderTimelineVo;
import com.example.demo.vo.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "订单")
@RestController
@RequestMapping("/order")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class OrderController {

    @Autowired
    private OrderMasterFacade orderMasterFacade;

    @ApiOperation("获取订单列表")
    @GetMapping("/list")
    public PageResult<List<OrderVo>> list(@Valid OrderPageRequest pageRequest) {
        Page<OrderMaster> page = orderMasterFacade.list(pageRequest);
        List<OrderVo> orderVoList = Convert.convert(new TypeReference<List<OrderVo>>() {
        }, page.getRecords());
        return PageResult.success(orderVoList, page.getTotal());
    }

    @ApiOperation("获取订单详情")
    @GetMapping("/{id}")
    public Result<Map<String, Object>> all(@PathVariable("id") Long id) {
        List<OrderDetail> orderDetailList = orderMasterFacade.getDetail(id);
        List<OrderTimeline> orderTimelineList = orderMasterFacade.getTimeline(id);

        List<OrderDetailVo> orderDetailVoList = Convert.convert(new TypeReference<List<OrderDetailVo>>() {
        }, orderDetailList);
        List<OrderTimelineVo> orderTimelineVoList = Convert.convert(new TypeReference<List<OrderTimelineVo>>() {
        }, orderTimelineList);

        Map<String, Object> map = new HashMap<>();
        map.put("detail", orderDetailVoList);
        map.put("timeline", orderTimelineVoList);
        return Result.success(map);
    }
}
