package com.example.demo.component;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.*;
import com.example.demo.utils.Constants;
import com.example.demo.dto.CartDto;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrderMasterService;
import com.example.demo.service.OrderTimelineService;
import com.example.demo.service.ProductService;
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
    private ProductService productService;

    @Autowired
    private OrderMasterService orderMasterService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderTimelineService orderTimelineService;

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

            // 获取信息
            Product product = productService.get(productId);

            // 创建订单详情
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setProductId(productId);
            orderDetail.setProductQuantity(productQuantity);
            orderDetail.setProductIcon(product.getIcon());
            orderDetail.setProductName(product.getName());
            orderDetail.setProductPrice(product.getPrice());

            // 添加订单详情
            orderDetailService.save(orderDetail);
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

        // 创建状态
        OrderTimeline orderTimeline = new OrderTimeline();
        orderTimeline.setOrderId(orderId);
        orderTimelineService.save(orderTimeline);

        // 发送通知
        mailService.send(user.getEmail(), orderMaster);
    }
}
