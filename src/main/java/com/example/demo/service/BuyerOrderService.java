package com.example.demo.service;

import com.example.demo.dto.page.OrderPageRequest;
import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.OrderTimeline;
import com.example.demo.model.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface BuyerOrderService {

    Order get(String username, Long id);

    List<OrderDetail> getDetail(String username, Long id);

    List<OrderTimeline> getTimeline(String username, Long id);

    PageInfo<Order> list(String username, OrderPageRequest pageRequest);

    String buy(User user);
}
