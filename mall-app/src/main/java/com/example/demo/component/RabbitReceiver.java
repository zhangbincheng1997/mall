package com.example.demo.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.enums.OrderStatusEnum;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrderMasterService;
import com.example.demo.service.OrderTimelineService;
import com.example.demo.service.ProductService;
import com.example.demo.utils.Constants;
import com.example.demo.entity.*;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Component
public class RabbitReceiver {

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
    private RabbitSender rabbitSender;

    // 这里都是正常操作，不会超卖
    @RabbitHandler
    @RabbitListener(queues = Constants.ORDER_TOPIC)
    @Transactional
    public void process(Message message, Channel channel) throws IOException {
        try {
            JSONObject jsonObject = JSONObject.parseObject(new String(message.getBody()));
            Long orderId = jsonObject.getLong("orderId");
            User user = JSONObject.parseObject(jsonObject.getString("user"), User.class);
            @SuppressWarnings("unchecked")
            Map<String, Integer> map = JSONObject.parseObject(jsonObject.getString("map"), Map.class);

            StringBuilder sb = new StringBuilder();
            sb.append("订单号：").append(orderId).append("\n");
            // 计算价格
            BigDecimal amount = new BigDecimal(0);
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                Long productId = Long.valueOf(entry.getKey());
                Integer productQuantity = entry.getValue();
                Product product = productService.get(productId);
                // 累加价格
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
            rabbitSender.send(Constants.ORDER_TTL_EXCHANGE,
                    Constants.ORDER_TTL_KEY,
                    orderId.toString(),
                    msg -> {
                        msg.getMessageProperties().setExpiration(Constants.ORDER_TTL); // 超时时间
                        return msg;
                    });
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); // 手动ACK
            log.info("创建订单成功");
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            log.info("创建订单失败");
        }
    }

    @RabbitHandler
    @RabbitListener(queues = Constants.ORDER_CANCEL_QUEUE)
    @Transactional
    public void processCancel(Message message, Channel channel) throws IOException {
        Long id = Long.valueOf(new String(message.getBody()));
        try {
            // 未支付，取消订单
            if (orderMasterService.getById(id).getStatus().equals(OrderStatusEnum.TO_BE_PAID.getCode())) {
                orderMasterService.addStockRedis(id); // 恢复预减库存
                orderMasterService.updateOrderStatus(id, OrderStatusEnum.CANCEL.getCode());
            }
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); // 手动ACK
            log.info("取消订单成功");
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            log.info("取消订单失败");
        }
    }
}
