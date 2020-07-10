package com.example.demo.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.component.RabbitSender;
import com.example.demo.component.redis.RedisService;
import com.example.demo.dto.CartDto;
import com.example.demo.entity.*;
import com.example.demo.component.CartService;
import com.example.demo.dto.page.OrderPageRequest;
import com.example.demo.mapper.OrderMasterMapper;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrderMasterService;
import com.example.demo.service.OrderTimelineService;
import com.example.demo.service.ProductService;
import com.example.demo.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderMasterServiceImpl extends ServiceImpl<OrderMasterMapper, OrderMaster> implements OrderMasterService {

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderTimelineService orderTimelineService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RabbitSender rabbitSender;

    private final DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(Constants.LUA_SCRIPT, Long.class);

    @Override
    public OrderMaster get(String username, Long id) {
        OrderMaster orderMaster = baseMapper.selectById(id);
        if (orderMaster != null && orderMaster.getUsername().equals(username)) return orderMaster;
        throw new GlobalException(Status.ORDER_NOT_EXIST);
    }

    private List<OrderDetail> getDetail(Long id) {
        return orderDetailService.list(Wrappers.<OrderDetail>lambdaQuery()
                .eq(OrderDetail::getOrderId, id));
    }

    @Override
    public List<OrderDetail> getDetail(String username, Long id) {
        get(username, id); // 保证存在
        return getDetail(id);
    }

    @Override
    public List<OrderTimeline> getTimeline(String username, Long id) {
        get(username, id); // 保证存在
        return orderTimelineService.list(Wrappers.<OrderTimeline>lambdaQuery()
                .eq(OrderTimeline::getOrderId, id));
    }

    @Override
    public Page<OrderMaster> list(String username, OrderPageRequest pageRequest) {
        Page<OrderMaster> page = new Page<>(pageRequest.getPage(), pageRequest.getLimit());
        QueryWrapper<OrderMaster> wrappers = new QueryWrapper<OrderMaster>()
                .eq("username", username);
        if (!StringUtils.isEmpty(pageRequest.getStatus()))
            wrappers.eq("status", pageRequest.getStatus());
        wrappers.orderByDesc("update_time", "id");
        return baseMapper.selectPage(page, wrappers);
    }

    @Override
    public String buy(User user) {
        // 购物车
        List<CartDto> cartDtoList = cartService.list(user.getUsername()).stream()
                .filter(CartDto::getChecked).collect(Collectors.toList()); // 下单部分
        if (cartDtoList.size() == 0) throw new GlobalException(Status.CART_EMPTY);

        // 记录购物ID和数量
        Map<String, Integer> map = new HashMap<>();
        List<String> keys = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        for (CartDto cartDto : cartDtoList) {
            map.put(cartDto.getId().toString(), cartDto.getQuantity());
            keys.add(Constants.PRODUCT_STOCK + cartDto.getId());
            values.add(cartDto.getQuantity());
        }
        Long result = redisService.execute(redisScript, keys, values.toArray());
        if (result == 0) throw new GlobalException(Status.PRODUCT_STOCK_NOT_ENOUGH);

        // 清空购物车
        List<Long> ids = cartDtoList.stream()
                .filter(CartDto::getChecked)
                .map(CartDto::getId).collect(Collectors.toList());
        cartService.delete(user.getUsername(), ids);

        JSONObject jsonStr = new JSONObject();
        Long orderId = snowflake.nextId(); // 雪花算法 生成全局唯一ID
        jsonStr.put("orderId", orderId);
        jsonStr.put("user", user);
        jsonStr.put("map", map);
        rabbitSender.send(Constants.ORDER_TOPIC, jsonStr.toJSONString()); // 发送消息队列
        return String.valueOf(orderId);
    }

    @Override
    public void updateOrderStatus(Long id, Integer status) {
        OrderMaster orderMaster = new OrderMaster()
                .setId(id)
                .setStatus(status);
        baseMapper.updateById(orderMaster);

        OrderTimeline orderTimeline = new OrderTimeline()
                .setOrderId(id)
                .setStatus(status);
        orderTimelineService.save(orderTimeline);
    }

    @Override
    @Transactional
    public void subStockMySQL(Long id) {
        List<OrderDetail> orderDetailList = getDetail(id);
        orderDetailList.forEach(orderDetail -> productService.subStock(orderDetail.getProductId(), orderDetail.getProductQuantity()));
    }

    @Override
    @Transactional
    public void addStockMySQL(Long id) {
        List<OrderDetail> orderDetailList = getDetail(id);
        orderDetailList.forEach(orderDetail -> productService.addStock(orderDetail.getProductId(), orderDetail.getProductQuantity()));
    }

    @Override
    public void addStockRedis(Long id) {
        List<OrderDetail> orderDetailList = getDetail(id);
        orderDetailList.forEach(orderDetail -> redisService.increment(Constants.PRODUCT_STOCK + orderDetail.getProductId(), orderDetail.getProductQuantity()));
    }

    @Override
    public void subStockRedis(Long id) {
        List<OrderDetail> orderDetailList = getDetail(id);
        orderDetailList.forEach(orderDetail -> redisService.decrement(Constants.PRODUCT_STOCK + orderDetail.getProductId(), orderDetail.getProductQuantity()));
    }
}
