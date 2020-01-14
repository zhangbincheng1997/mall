package com.example.demo.service.impl;

import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.dto.page.OrderPageRequest;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.mapper.OrderTimelineMapper;
import com.example.demo.model.*;
import com.example.demo.service.SellerOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class SellerOrderServiceImpl implements SellerOrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderTimelineMapper orderTimelineMapper;

    @Override
    public Order get(Long id) {
        Order orderMaster = orderMapper.selectByPrimaryKey(id);
        if (orderMaster == null) throw new GlobalException(Status.ORDER_NOT_EXIST);
        return orderMaster;
    }

    @Override
    public List<OrderDetail> getDetail(Long id) {
        get(id); // 保证存在
        OrderDetailExample example = new OrderDetailExample();
        example.createCriteria().andOrderIdEqualTo(id);
        return orderDetailMapper.selectByExample(example);
    }

    @Override
    public List<OrderTimeline> getTimeline(Long id) {
        get(id); // 保证存在
        OrderTimelineExample example = new OrderTimelineExample();
        example.createCriteria().andOrderIdEqualTo(id);
        return orderTimelineMapper.selectByExample(example);
    }

    @Override
    public PageInfo<Order> list(OrderPageRequest pageRequest) {
        OrderExample example = new OrderExample();
        OrderExample.Criteria criteria = example.createCriteria();

        String status = pageRequest.getStatus();
        if (!StringUtils.isEmpty(status)) {
            criteria.andStatusEqualTo(new Integer(status));
        }

        PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit(), "update_time desc, id desc");
        List<Order> orderList = orderMapper.selectByExample(example);
        return new PageInfo<Order>(orderList);
    }
}
