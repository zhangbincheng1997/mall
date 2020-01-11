package com.example.demo.service;

import com.example.demo.model.OrderDetail;

import java.util.List;

public interface OrderService {

    List<OrderDetail> getDetail(Long id);

    int updateOrderStatus(Long id, Integer orderStatus);

    void increaseStock(Long id);
    void decreaseStock(Long id);
    void addStockRedis(Long id);
}
