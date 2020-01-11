package com.example.demo.service;

import com.example.demo.dto.OrderMasterDto;
import com.example.demo.dto.page.OrderPageRequest;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.OrderMaster;
import com.example.demo.model.User;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface BuyerOrderService {

    OrderMaster get(String username, Long id);

    List<OrderDetail> getDetail(String username, Long id);

    PageInfo<OrderMaster> list(String username, OrderPageRequest pageRequest);

    String buy(User user, OrderMasterDto orderMasterDto);
}
