package com.example.demo.service.impl;

import com.example.demo.component.RedisLocker;
import com.example.demo.component.RedisService;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.mapper.OrderMasterMapper;
import com.example.demo.mapper.ProductCustomMapper;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.OrderDetailExample;
import com.example.demo.model.OrderMaster;
import com.example.demo.model.OrderMasterExample;
import com.example.demo.service.OrderService;
import com.example.demo.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisLocker redisLocker;

    @Autowired
    private ProductCustomMapper productCustomMapper;

    @Autowired
    private OrderMasterMapper orderMasterMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public List<OrderDetail> getDetail(Long id) {
        OrderDetailExample example = new OrderDetailExample();
        example.createCriteria().andOrderIdEqualTo(id);
        return orderDetailMapper.selectByExample(example);
    }

    @Override
    public int updateOrderStatus(Long id, Integer orderStatus) {
        OrderMaster order = new OrderMaster();
        order.setOrderStatus(orderStatus);
        OrderMasterExample example = new OrderMasterExample();
        example.createCriteria().andIdEqualTo(id);
        return orderMasterMapper.updateByExampleSelective(order, example);
    }

    // 修改数据库商品数量
    @Override
    @Transactional
    public void increaseStock(Long id) {
        List<OrderDetail> orderDetailList = getDetail(id);
        // lock
        orderDetailList.forEach(orderDetail -> {
            redisLocker.lock(Constants.REDIS_PRODUCT_MYSQL_LOCK + orderDetail.getProductId());
            productCustomMapper.increaseStock(orderDetail.getProductId(), orderDetail.getProductQuantity());
            redisLocker.unlock(Constants.REDIS_PRODUCT_MYSQL_LOCK + orderDetail.getProductId());
        });
    }

    @Override
    @Transactional
    public void decreaseStock(Long id) {
        List<OrderDetail> orderDetailList = getDetail(id);
        // lock
        orderDetailList.forEach(orderDetail -> {
            redisLocker.lock(Constants.REDIS_PRODUCT_MYSQL_LOCK + orderDetail.getProductId());
            productCustomMapper.decreaseStock(orderDetail.getProductId(), orderDetail.getProductQuantity());
            redisLocker.unlock(Constants.REDIS_PRODUCT_MYSQL_LOCK + orderDetail.getProductId());
        });
    }

    @Override
    @Transactional
    public void addStockRedis(Long id) {
        List<OrderDetail> orderDetailList = getDetail(id);
        // lock
        orderDetailList.forEach(orderDetail -> {
            redisLocker.lock(Constants.REDIS_PRODUCT_REDIS_LOCK + orderDetail.getProductId());
            redisService.increment(Constants.REDIS_PRODUCT_STOCK + orderDetail.getProductId(), orderDetail.getProductQuantity());
            redisLocker.lock(Constants.REDIS_PRODUCT_REDIS_LOCK + orderDetail.getProductId());
        });
    }
}
