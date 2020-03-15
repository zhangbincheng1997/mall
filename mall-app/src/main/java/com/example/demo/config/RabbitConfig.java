package com.example.demo.config;

import com.example.demo.common.utils.Constants;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue orderQueue() {
        return new Queue(Constants.ORDER_TOPIC);
    }
}
