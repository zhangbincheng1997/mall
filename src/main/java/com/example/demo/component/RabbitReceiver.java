package com.example.demo.component;

import cn.hutool.core.lang.Snowflake;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dto.OrderDetailDto;
import com.example.demo.dto.OrderMasterDto;
import com.example.demo.mapper.OrderDetailMapper;
import com.example.demo.mapper.OrderMasterMapper;
import com.example.demo.mapper.ProductCustomMapper;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.OrderMaster;
import com.example.demo.model.Product;
import com.example.demo.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class RabbitReceiver {

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisLocker redisLocker;

    @Autowired
    private ProductCustomMapper productCustomMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderMasterMapper orderMasterMapper;

    // 这里都是正常操作，不会超卖
    @RabbitHandler
    @RabbitListener(queues = Constants.ORDER_TOPIC)
    public void process(String message) {
        JSONObject jsonObject = JSONObject.parseObject(message);
        String username = (String) jsonObject.get("username");
        OrderMasterDto orderMasterDto = (OrderMasterDto) jsonObject.get("orderMasterDto");
        String uuid = (String) jsonObject.get("uuid");

        // 雪花算法 生成全局唯一ID
        Long orderId = snowflake.nextId();
        // 计算价格
        BigDecimal amount = new BigDecimal(0);
        for (OrderDetailDto orderDetailDto : orderMasterDto.getProducts()) {
            Long productId = orderDetailDto.getId();
            Integer productQuantity = orderDetailDto.getQuantity();

            // 创建订单详情
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setProductId(productId);
            orderDetail.setProductQuantity(productQuantity);
            // 获取信息
            JSONObject product = (JSONObject) redisService.get(Constants.REDIS_PRODUCT_INFO + productId);
            orderDetail.setProductIcon(product.getString("icon"));
            orderDetail.setProductName(product.getString("name"));
            orderDetail.setProductPrice(product.getBigDecimal("price"));

            // 添加订单详情
            orderDetailMapper.insertSelective(orderDetail);
            // 累加价格
            amount = amount.add(product.getBigDecimal("price").multiply(new BigDecimal(productQuantity)));
        }
        // 创建订单
        OrderMaster order = new OrderMaster();
        order.setUsername(username);
        order.setId(orderId);
        order.setAmount(amount);
        // 添加订单
        orderMasterMapper.insertSelective(order);

        // 加入缓存
        redisService.set(Constants.REDIS_PRODUCT_POLLING + uuid, order);
    }
}
