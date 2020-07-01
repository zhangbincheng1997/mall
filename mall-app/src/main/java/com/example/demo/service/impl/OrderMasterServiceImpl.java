package com.example.demo.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.component.MailService;
import com.example.demo.component.RabbitSender;
import com.example.demo.component.redis.RedisService;
import com.example.demo.dao.StockDao;
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
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderMasterServiceImpl extends ServiceImpl<OrderMasterMapper, OrderMaster> implements OrderMasterService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RabbitSender rabbitSender;

    @Autowired
    private CartService cartService;

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private StockDao stockDao;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderTimelineService orderTimelineService;

    private DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(Constants.LUA_SCRIPT, Long.class);

    @Override
    public OrderMaster get(String username, Long id) {
        OrderMaster orderMaster = baseMapper.selectById(id);
        if (orderMaster != null && orderMaster.getUsername().equals(username))
            return orderMaster;
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

    @Autowired
    private MailService mailService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderMasterService orderMasterService;

    @Override
    @Transactional
    public String buy0(User user) {
        // 购物车
        List<CartDto> cartDtoList = cartService.list(user.getUsername()).stream()
                .filter(CartDto::getChecked).collect(Collectors.toList()); // 下单部分
        if (cartDtoList.size() == 0) throw new GlobalException(Status.CART_EMPTY);

        Long orderId = snowflake.nextId(); // 雪花算法 生成全局唯一ID

        StringBuilder sb = new StringBuilder();
        sb.append("订单号：").append(orderId).append("\n");
        // 计算价格
        BigDecimal amount = new BigDecimal(0);
        for (CartDto cartDto : cartDtoList) {
            Long productId = cartDto.getId();
            Integer productQuantity = cartDto.getQuantity();
            int result = stockDao.decreaseStock(productId, productQuantity); // 减库存
            if (result != 1) throw new GlobalException(Status.PRODUCT_STOCK_NOT_ENOUGH); // 回滚

            Product product = productService.get(productId);

            // 创建订单详情
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setProductId(productId);
            orderDetail.setProductQuantity(productQuantity);
            orderDetail.setProductIcon(product.getIcon());
            orderDetail.setProductName(product.getName());
            orderDetail.setProductPrice(product.getPrice());
            orderDetailService.save(orderDetail);
            sb.append(JSON.toJSONString(orderDetail)).append("\n");

            // 累加价格
            amount = amount.add(product.getPrice().multiply(new BigDecimal(productQuantity)));
        }
        // 创建订单
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setId(orderId);
        orderMaster.setAmount(amount);
        orderMaster.setUsername(user.getUsername());
        orderMaster.setNickname(user.getNickname());
        orderMaster.setEmail(user.getEmail());
        orderMasterService.save(orderMaster);
        sb.append("总价：").append(amount).append("\n");
        // 创建订单状态
        OrderTimeline orderTimeline = new OrderTimeline();
        orderTimeline.setOrderId(orderId);
        orderTimelineService.save(orderTimeline);
        // 发送通知
        mailService.send(user.getEmail(), sb.toString());
        // 订单超时
        rabbitSender.send(Constants.ORDER_TTL_EXCHANGE,
                Constants.ORDER_TTL_KEY,
                orderId.toString(),
                msg -> {
                    msg.getMessageProperties().setExpiration(Constants.ORDER_TTL); // 超时时间
                    return msg;
                });

        // 清空购物车
        List<Long> ids = cartDtoList.stream().map(CartDto::getId).collect(Collectors.toList());
        cartService.delete(user.getUsername(), ids);

        return String.valueOf(orderId);
    }

    @Override
    public String buy(User user) {
        // 购物车
        List<CartDto> cartDtoList = cartService.list(user.getUsername()).stream()
                .filter(CartDto::getChecked).collect(Collectors.toList()); // 下单部分
        if (cartDtoList.size() == 0) throw new GlobalException(Status.CART_EMPTY);

        // 记录购物ID和数量
        Map<String, Integer> map = new HashMap<>();
        // 检查数量
        for (CartDto cartDto : cartDtoList) {
            Long productId = cartDto.getId();
            Integer productQuantity = cartDto.getQuantity();
            // 预减库存
            Long result = redisService.execute(redisScript,
                    Collections.singletonList(Constants.PRODUCT_STOCK + productId),
                    productQuantity);
            if (result == -1) {
                // 恢复库存
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    redisService.increment(Constants.PRODUCT_STOCK + entry.getKey(), entry.getValue());
                }
                // 库存不足
                throw new GlobalException(Status.PRODUCT_STOCK_NOT_ENOUGH);
            }
            map.put(productId.toString(), productQuantity);
        }

        // 清空购物车
        List<Long> ids = cartDtoList.stream().map(CartDto::getId).collect(Collectors.toList());
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
    public void decreaseStock(Long id) {
        List<OrderDetail> orderDetailList = getDetail(id);
        orderDetailList.forEach(orderDetail -> {
            stockDao.decreaseStock(orderDetail.getProductId(), orderDetail.getProductQuantity());
        });
    }

    @Override
    @Transactional
    public void addStockRedis(Long id) {
        List<OrderDetail> orderDetailList = getDetail(id);
        orderDetailList.forEach(orderDetail -> {
            redisService.increment(Constants.PRODUCT_STOCK + orderDetail.getProductId(),
                    orderDetail.getProductQuantity());
        });
    }
}
