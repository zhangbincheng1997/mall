package com.example.demo.service;

import com.example.demo.base.PageResult;
import com.example.demo.dto.OrderMasterDto;
import com.example.demo.dto.PageRequest;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.OrderMaster;
import com.example.demo.vo.OrderMasterVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface OrderService {

    List<OrderDetail> getDetail(Long id);

    /**
     * Seller
     */
    OrderMaster get(Long id);

    PageInfo<OrderMaster> list(PageRequest pageRequest);

    int updateOrderStatus(Long id, Integer orderStatus);

    int updatePayStatus(Long id, Integer payStatus);

    void increaseStock(Long id);
    void decreaseStock(Long id);
    void addStockRedis(Long id);

    /**
     * Buyer
     */
    OrderMaster getByBuyer(String username, Long id);

    PageInfo<OrderMaster> listByBuyer(String username, PageRequest pageRequest);

    String buy(String username, OrderMasterDto orderMasterDto);

    OrderMasterVo polling(String uuid);
}
