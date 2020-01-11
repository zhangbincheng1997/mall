package com.example.demo.service;

import com.example.demo.dto.page.OrderPageRequest;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.OrderMaster;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SellerOrderService {

    OrderMaster get(Long id);

    List<OrderDetail> getDetail(Long id);

    PageInfo<OrderMaster> list(OrderPageRequest pageRequest);
}
