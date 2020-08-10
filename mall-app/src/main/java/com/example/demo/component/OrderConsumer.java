package com.example.demo.component;

import com.alibaba.fastjson.JSON;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrderMasterService;
import com.example.demo.service.OrderTimelineService;
import com.example.demo.service.ProductService;
import com.example.demo.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = OrderMessage.TOPIC + "-group", topic = OrderMessage.TOPIC)
public class OrderConsumer implements RocketMQListener<OrderMessage>  {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderMasterService orderMasterService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderTimelineService orderTimelineService;

    @Autowired
    private MailService mailService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    // 这里都是正常操作，不会超卖
    @Transactional
    @Override
    public void onMessage(OrderMessage orderMessage) {
        try {
            Long orderId = orderMessage.getOrderId();
            User user = orderMessage.getUser();
            Map<String, Integer> cart = orderMessage.getCart();

            StringBuilder sb = new StringBuilder();
            sb.append("订单号：").append(orderId).append("\n");
            // 计算价格
            BigDecimal amount = new BigDecimal(0);
            for (Map.Entry<String, Integer> entry : cart.entrySet()) {
                Long productId = Long.valueOf(entry.getKey());
                Integer productQuantity = entry.getValue();
                productService.subStock(productId, productQuantity); // 真正减库存
                // 累加价格
                Product product = productService.get(productId);
                amount = amount.add(product.getPrice().multiply(new BigDecimal(productQuantity)));
                sb.append("总价：").append(amount).append("\n");

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
            }
            // 创建订单
            OrderMaster orderMaster = new OrderMaster();
            orderMaster.setId(orderId);
            orderMaster.setAmount(amount);
            orderMaster.setUsername(user.getUsername());
            orderMaster.setNickname(user.getNickname());
            orderMaster.setEmail(user.getEmail());
            orderMasterService.save(orderMaster);
            // 创建订单状态
            OrderTimeline orderTimeline = new OrderTimeline();
            orderTimeline.setOrderId(orderId);
            orderTimelineService.save(orderTimeline);
            // 发送通知
            mailService.send(user.getEmail(), sb.toString());
            // 订单超时
            // 5 - 1min
            // 9 - 5min
            // 14 - 10min
            rocketMQTemplate.syncSend(DelayMessage.TOPIC,
                    MessageBuilder.withPayload(orderMessage).build(),
                    30 * 1000, 5);
            log.info("创建订单成功");
        } catch (Exception e) {
            log.info("创建订单失败");
        }
    }
}
