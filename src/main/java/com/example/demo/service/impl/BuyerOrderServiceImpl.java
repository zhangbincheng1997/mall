package com.example.demo.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.component.RabbitSender;
import com.example.demo.component.RedisLocker;
import com.example.demo.component.RedisService;
import com.example.demo.dto.CartDto;
import com.example.demo.dto.page.OrderPageRequest;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.mapper.OrderMasterMapper;
import com.example.demo.mapper.OrderTimelineMapper;
import com.example.demo.model.*;
import com.example.demo.service.BuyerOrderService;
import com.example.demo.service.CartService;
import com.example.demo.utils.Constants;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonMultiLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BuyerOrderServiceImpl implements BuyerOrderService {

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisLocker redisLocker;

    @Autowired
    private RabbitSender rabbitSender;

    @Autowired
    private OrderMasterMapper orderMasterMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderTimelineMapper orderTimelineMapper;

    @Autowired
    private CartService cartService;

    @Override
    public OrderMaster get(String username, Long id) {
        OrderMasterExample example = new OrderMasterExample();
        example.createCriteria().andIdEqualTo(id).andUsernameEqualTo(username);
        List<OrderMaster> orderList = orderMasterMapper.selectByExample(example);
        if (orderList.size() == 0) throw new GlobalException(Status.ORDER_NOT_EXIST);
        return orderList.get(0);
    }

    @Override
    public List<OrderDetail> getDetail(String username, Long id) {
        get(username, id); // 保证存在
        OrderDetailExample example = new OrderDetailExample();
        example.createCriteria().andOrderIdEqualTo(id);
        return orderDetailMapper.selectByExample(example);
    }

    @Override
    public List<OrderTimeline> getTimeline(String username, Long id) {
        get(username, id); // 保证存在
        OrderTimelineExample example = new OrderTimelineExample();
        example.createCriteria().andOrderIdEqualTo(id);
        return orderTimelineMapper.selectByExample(example);
    }

    @Override
    public PageInfo<OrderMaster> list(String username, OrderPageRequest pageRequest) {
        OrderMasterExample example = new OrderMasterExample();
        OrderMasterExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);

        String status = pageRequest.getStatus();
        if (!StringUtils.isEmpty(status)) {
            criteria.andStatusEqualTo(new Integer(status));
        }

        PageHelper.startPage(pageRequest.getPage(), pageRequest.getLimit(), "update_time desc, id desc");
        List<OrderMaster> orderList = orderMasterMapper.selectByExample(example);
        return new PageInfo<OrderMaster>(orderList);
    }

    /**
     * https://github.com/coderliguoqing/distributed-seckill
     * 创建订单：1.校验状态 2.redis减库存 3.MQ创建订单
     * 支付回调：1.校验状态 2.mysql减库存
     * 取消支付：1.校验状态 2.redis回退
     * 订单退款：1.校验状态 2.redis回退 3.mysql回退
     *
     * @param user
     * @return
     */
    @Override
    public String buy(User user) {
        List<CartDto> cartDtoList = cartService.list(user.getUsername()).stream()
                .filter(CartDto::getChecked).collect(Collectors.toList()); // 下单部分
        List<String> lockKey = cartDtoList.stream()
                .map(cartDto -> Constants.REDIS_PRODUCT_REDIS_LOCK + cartDto.getId())
                .collect(Collectors.toList());

        // lock
        RedissonMultiLock multiLock = redisLocker.multiLock(Convert.toStrArray(lockKey));
        // 检查数量
        for (CartDto cartDto : cartDtoList) {
            Long productId = cartDto.getId();
            Integer productQuantity = cartDto.getQuantity();
            Integer stock = (Integer) redisService.get(Constants.REDIS_PRODUCT_STOCK + productId);
            // 商品不存在
            if (stock == null) throw new GlobalException(Status.PRODUCT_NOT_EXIST);
            // 库存不足
            if (stock < productQuantity) throw new GlobalException(Status.PRODUCT_STOCK_NOT_ENOUGH);
        }
        // 预减库存
        for (CartDto cartDto : cartDtoList) {
            Long productId = cartDto.getId();
            Integer productQuantity = cartDto.getQuantity();
            redisService.decrement(Constants.REDIS_PRODUCT_STOCK + productId, productQuantity);
        }
        // unlock
        multiLock.unlock();

        // 清空购物车
        List<Long> ids = cartDtoList.stream().map(CartDto::getId).collect(Collectors.toList());
        cartService.delete(user.getUsername(), ids);

        JSONObject jsonStr = new JSONObject();
        Long orderId = snowflake.nextId(); // 雪花算法 生成全局唯一ID
        jsonStr.put("orderId", orderId);
        jsonStr.put("user", user);
        jsonStr.put("cartDtoList", cartDtoList);
        rabbitSender.send(Constants.ORDER_TOPIC, jsonStr.toJSONString()); // 发送消息队列
        return String.valueOf(orderId);
    }
}
