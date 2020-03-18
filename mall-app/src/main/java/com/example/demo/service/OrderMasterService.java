package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.page.OrderPageRequest;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.OrderMaster;
import com.example.demo.entity.OrderTimeline;
import com.example.demo.entity.User;

import java.util.List;

public interface OrderMasterService extends IService<OrderMaster> {

    OrderMaster get(String username, Long id);

    List<OrderDetail> getDetail(String username, Long id);

    List<OrderTimeline> getTimeline(String username, Long id);

    Page<OrderMaster> list(String username, OrderPageRequest pageRequest);

    String buy(User user);

    void updateOrderStatus(Long id, Integer status);

    void decreaseStock(Long id);

    void addStockRedis(Long id);
}
