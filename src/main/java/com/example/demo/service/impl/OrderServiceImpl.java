package com.example.demo.service.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.component.RabbitSender;
import com.example.demo.component.RedisLocker;
import com.example.demo.component.RedisService;
import com.example.demo.dto.OrderDetailDto;
import com.example.demo.dto.OrderMasterDto;
import com.example.demo.dto.PageRequest;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.mapper.OrderMasterMapper;
import com.example.demo.mapper.ProductCustomMapper;
import com.example.demo.model.*;
import com.example.demo.service.OrderService;
import com.example.demo.utils.Constants;
import com.example.demo.vo.OrderMasterVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisLocker redisLocker;

    @Autowired
    private RabbitSender rabbitSender;

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

    /**
     * Seller
     */
    @Override
    @Cacheable(cacheNames = "order", key = "#id")
    public OrderMaster get(Long id) {
        OrderMaster orderMaster = orderMasterMapper.selectByPrimaryKey(id);
        if (orderMaster == null) throw new GlobalException(Status.ORDER_NOT_EXIST);
        return orderMaster;
    }

    @Override
    public PageInfo<OrderMaster> list(PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit(), "update_time desc, id desc");
        String keyword = pageRequest.getKeyword();
        OrderMasterExample example = new OrderMasterExample();
        Long id = Convert.toLong(keyword);
        if (id != null) {
            example.or().andIdEqualTo(id);
        }
        List<OrderMaster> orderList = orderMasterMapper.selectByExample(example);
        return new PageInfo<OrderMaster>(orderList);
    }

    @Override
    @CacheEvict(cacheNames = "order", key = "#id")
    public int updateOrderStatus(Long id, Integer orderStatus) {
        OrderMaster order = new OrderMaster();
        order.setOrderStatus(orderStatus);
        OrderMasterExample example = new OrderMasterExample();
        example.createCriteria().andIdEqualTo(id);
        return orderMasterMapper.updateByExampleSelective(order, example);
    }

    @Override
    @CacheEvict(cacheNames = "order", key = "#id")
    public int updatePayStatus(Long id, Integer payStatus) {
        OrderMaster order = new OrderMaster();
        order.setPayStatus(payStatus);
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

    /**
     * Buyer
     */
    @Override
    @Cacheable(cacheNames = "order", key = "#id")
    public OrderMaster getByBuyer(String username, Long id) {
        OrderMasterExample example = new OrderMasterExample();
        example.createCriteria().andIdEqualTo(id).andUsernameEqualTo(username);
        List<OrderMaster> orderList = orderMasterMapper.selectByExample(example);
        if (orderList.size() == 0) throw new GlobalException(Status.ORDER_NOT_EXIST);
        return orderList.get(0);
    }

    @Override
    public PageInfo<OrderMaster> listByBuyer(String username, PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit(), "update_time desc, id desc");
        String keyword = pageRequest.getKeyword();
        OrderMasterExample example = new OrderMasterExample();
        example.createCriteria().andUsernameEqualTo(username);
        Long id = Convert.toLong(keyword);
        if (id != null) {
            example.getOredCriteria().get(0).andIdEqualTo(id);
        }
        List<OrderMaster> orderList = orderMasterMapper.selectByExample(example);
        return new PageInfo<OrderMaster>(orderList);
    }

    /**
     * https://github.com/coderliguoqing/distributed-seckill
     * 创建订单：1.校验状态 redis减库存 3.MQ创建订单 4.轮询查询订单
     * 支付回调：1.校验状态 2.修改订单状态 3.mysql减库存
     * 取消支付：1.校验状态 2.修改订单状态 3.redis回退
     * 订单退款：1.校验状态 2.修改订单状态 3.redis回退 4.mysql回退
     *
     * @param username
     * @param orderMasterDto
     * @return
     */
    @Override
    @Transactional // 事务ACID
    public String buy(String username, OrderMasterDto orderMasterDto) {
        // 判断是否可以下单
        for (OrderDetailDto orderDetailDto : orderMasterDto.getProducts()) {
            Long productId = orderDetailDto.getId();
            Integer productQuantity = orderDetailDto.getQuantity();
            // lock
            redisLocker.lock(Constants.REDIS_PRODUCT_REDIS_LOCK + productId, Constants.REDIS_LOCK_LEASE_TIME);

            // 从redis获取
            Integer stock = (Integer) redisService.get(Constants.REDIS_PRODUCT_STOCK + productId);
            // 商品不存在
            if (stock == null) throw new GlobalException(Status.PRODUCT_NOT_EXIST);
            // 库存不足
            if (stock < productQuantity) throw new GlobalException(Status.PRODUCT_STOCK_NOT_ENOUGH);

            // 预减库存
            redisService.decrement(Constants.REDIS_PRODUCT_INFO + productId, productQuantity);

            // unlock
            redisLocker.unlock(Constants.REDIS_PRODUCT_REDIS_LOCK + productId);
        }
        String uuid = UUID.randomUUID().toString();
        redisService.set(Constants.REDIS_PRODUCT_POLLING + uuid, null); // 设置轮询记录

        JSONObject jsonStr = new JSONObject();
        jsonStr.put("username", username);
        jsonStr.put("orderMasterDto", orderMasterDto);
        jsonStr.put("uuid", uuid);
        rabbitSender.send(Constants.ORDER_TOPIC, jsonStr.toJSONString()); // 发送消息队列

        return uuid;
    }

    // 轮询是否存在
    // 存在则继续轮询
    @Override
    public OrderMasterVo polling(String uuid) {
        OrderMaster orderMaster = (OrderMaster) redisService.get(Constants.REDIS_PRODUCT_POLLING + uuid);
        if (orderMaster == null) return null;
        else return Convert.convert(OrderMasterVo.class, orderMaster);
    }
}
