package com.example.demo.service;

import com.example.demo.base.PageResult;
import com.example.demo.dto.OrderMasterDto;
import com.example.demo.dto.PageRequest;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.OrderMaster;
import com.example.demo.vo.OrderMasterVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SellerOrderService {

    OrderMaster get(Long id);

    PageInfo<OrderMaster> list(PageRequest pageRequest);
}
