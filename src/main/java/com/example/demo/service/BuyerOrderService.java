package com.example.demo.service;

import com.example.demo.dto.OrderMasterDto;
import com.example.demo.dto.PageRequest;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.OrderMaster;
import com.example.demo.vo.OrderMasterVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface BuyerOrderService {

    OrderMaster get(String username, Long id);

    PageInfo<OrderMaster> list(String username, PageRequest pageRequest);

    String buy(String username, OrderMasterDto orderMasterDto);

    OrderMasterVo polling(String uuid);
}
