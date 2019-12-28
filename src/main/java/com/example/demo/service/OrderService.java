package com.example.demo.service;

import com.example.demo.base.PageResult;
import com.example.demo.dto.OrderMasterDto;
import com.example.demo.dto.PageRequest;
import com.example.demo.vo.OrderMasterVo;

public interface OrderService {

    /**
     * Seller
     */
    OrderMasterVo get(Long id);

    PageResult list(PageRequest pageRequest);

    int updateOrderStatus(Long id, Integer orderStatus);

    int updatePayStatus(Long id, Integer payStatus);

    /**
     * Buyer
     */
    OrderMasterVo getByBuyer(String username, Long id);

    PageResult listByBuyer(String username, PageRequest pageRequest);

    Long buy(String username, OrderMasterDto orderMasterDto);
}
