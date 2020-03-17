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
import com.example.demo.service.OrderMasterService;
import com.example.demo.vo.OrderDetailVo;
import com.example.demo.vo.OrderTimelineVo;
import com.example.demo.vo.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "买家订单")
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderMasterService orderMasterService;

    @ApiOperation("获取订单列表")
    @GetMapping("/list")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public PageResult<List<OrderVo>> list(@ApiIgnore Principal principal, @Valid OrderPageRequest pageRequest) {
        // page
        Page<OrderMaster> page = orderMasterService.list(principal.getName(), pageRequest);
        // convert
        List<OrderVo> orderVoList = Convert.convert(new TypeReference<List<OrderVo>>() {
        }, page.getRecords());
        return PageResult.success(orderVoList, page.getTotal());
    }

    @ApiOperation("获取订单详情")
    @GetMapping("/{id}")
    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public Result<Map<String, Object>> all(@ApiIgnore Principal principal, @PathVariable("id") Long id) {
        List<OrderDetail> orderDetailList = orderMasterService.getDetail(principal.getName(), id);
        List<OrderTimeline> orderTimelineList = orderMasterService.getTimeline(principal.getName(), id);

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
