package com.example.demo.component;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dto.CartDto;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.mapper.OrderMasterMapper;
import com.example.demo.mapper.OrderTimelineMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.*;
import com.example.demo.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
public class RabbitReceiver {

    @Autowired
    private MailService mailService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderTimelineMapper orderTimelineMapper;

    @Autowired
    private OrderMasterMapper orderMasterMapper;

    // 这里都是正常操作，不会超卖
    @RabbitHandler
    @RabbitListener(queues = Constants.ORDER_TOPIC)
    public void process(String message) {
        JSONObject jsonObject = JSONObject.parseObject(message);
        Long orderId = (Long) jsonObject.get("orderId");
        User user = JSONObject.parseObject(jsonObject.getString("user"), User.class);
        List<CartDto> cartDtoList = JSONObject.parseArray(jsonObject.getString("cartDtoList"), CartDto.class);

        // 计算价格
        BigDecimal amount = new BigDecimal(0);
        for (CartDto cartDto : cartDtoList) {
            Long productId = cartDto.getId();
            Integer productQuantity = cartDto.getQuantity();

            // 创建订单详情
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setProductId(productId);
            orderDetail.setProductQuantity(productQuantity);
            // 获取信息
            Product product = productMapper.selectByPrimaryKey(productId);
            orderDetail.setProductIcon(product.getIcon());
            orderDetail.setProductName(product.getName());
            orderDetail.setProductPrice(product.getPrice());

            // 添加订单详情
            orderDetailMapper.insertSelective(orderDetail);
            // 累加价格
            amount = amount.add(product.getPrice().multiply(new BigDecimal(productQuantity)));
        }
        // 创建订单
        OrderMaster order = new OrderMaster();
        order.setUsername(user.getUsername());
        order.setNickname(user.getNickname());
        order.setEmail(user.getEmail());
        order.setId(orderId);
        order.setAmount(amount);
        orderMasterMapper.insertSelective(order);

        // 创建状态
        OrderTimeline orderTimeline = new OrderTimeline();
        orderTimeline.setOrderId(orderId);
        orderTimelineMapper.insertSelective(orderTimeline);

        // 发送通知
        mailService.send(user.getEmail(), order);
    }
}
