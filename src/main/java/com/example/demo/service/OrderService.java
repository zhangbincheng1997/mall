package com.example.demo.service;

import com.example.demo.dto.PageRequest;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.OrderMaster;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface OrderService {

    List<OrderDetail> getDetail(Long id);

    int updateOrderStatus(Long id, Integer orderStatus);
    int updatePayStatus(Long id, Integer payStatus);

    void increaseStock(Long id);
    void decreaseStock(Long id);
    void addStockRedis(Long id);
}
