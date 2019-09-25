package com.example.demo.config;

import com.example.demo.utils.Constants;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue testQueue() {
        return new Queue(Constants.TEST_TOPIC);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(Constants.EMAIL_TOPIC);
    }
}
