package com.example.demo.service;

import com.example.demo.dto.OrderMasterDto;
import com.example.demo.dto.PageRequest;
import com.example.demo.vo.OrderMasterVo;
import com.github.pagehelper.PageInfo;

public interface OrderService {

    /**
     * Seller
     */
    OrderMasterVo get(Long id);

    PageInfo<OrderMasterVo> list(PageRequest pageRequest);

    int updateOrderStatus(Long id, Integer orderStatus);

    int updatePayStatus(Long id, Integer payStatus);

    /**
     * Buyer
     */
    OrderMasterVo getByBuyer(String username, Long id);

    PageInfo<OrderMasterVo> listByBuyer(String username, PageRequest pageRequest);

    Long buy(String username, OrderMasterDto orderMasterDto);
}
