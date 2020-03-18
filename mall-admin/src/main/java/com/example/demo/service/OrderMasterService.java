package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.page.OrderPageRequest;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.OrderMaster;
import com.example.demo.entity.OrderTimeline;

import java.util.List;

public interface OrderMasterService extends IService<OrderMaster> {

    OrderMaster get(Long id);

    List<OrderDetail> getDetail(Long id);

    List<OrderTimeline> getTimeline(Long id);

    Page<OrderMaster> list(OrderPageRequest pageRequest);

    void updateOrderStatus(Long id, Integer status);

    void increaseStock(Long id);

    void addStockRedis(Long id);
}
