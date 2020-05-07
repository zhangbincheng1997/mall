package com.example.demo.config;

import com.example.demo.utils.Constants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    // 创建订单队列
    @Bean
    public Queue orderQueue() {
        return new Queue(Constants.ORDER_TOPIC, true);
    }

    // 延迟消费队列
    @Bean
    Queue orderTTLQueue() {
        Map<String, Object> params = new HashMap<>();
        // 过期后转发到 DLX
        params.put("x-dead-letter-exchange", Constants.ORDER_CANCEL_EXCHANGE);
        // 过期后转发到 routingKey
        params.put("x-dead-letter-routing-key", Constants.ORDER_CANCEL_KEY);
        return new Queue(Constants.ORDER_TTL_QUEUE, true, false, false, params);
    }

    @Bean
    DirectExchange orderTTLExchange() {
        return new DirectExchange(Constants.ORDER_TTL_EXCHANGE);
    }

    @Bean
    Binding orderTTLBinding() {
        return BindingBuilder.bind(orderTTLQueue())
                .to(orderTTLExchange())
                .with(Constants.ORDER_TTL_KEY);
    }

    // 取消订单队列
    @Bean
    public Queue orderCancelQueue() {
        return new Queue(Constants.ORDER_CANCEL_QUEUE, true);
    }

    @Bean
    DirectExchange orderCancelExchange() {
        return new DirectExchange(Constants.ORDER_CANCEL_EXCHANGE);
    }

    @Bean
    public Binding orderCancelBinding() {
        return BindingBuilder.bind(orderCancelQueue())
                .to(orderCancelExchange())
                .with(Constants.ORDER_CANCEL_KEY);
    }
}
