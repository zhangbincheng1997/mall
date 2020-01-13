package com.example.demo.service;

import com.example.demo.dto.page.OrderPageRequest;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.OrderMaster;
import com.example.demo.model.OrderTimeline;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SellerOrderService {

    OrderMaster get(Long id);

    List<OrderDetail> getDetail(Long id);

    List<OrderTimeline> getTimeline(Long id);

    PageInfo<OrderMaster> list(OrderPageRequest pageRequest);
}
