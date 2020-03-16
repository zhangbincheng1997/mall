package com.example.demo.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String topic, String obj) {
        this.rabbitTemplate.convertAndSend(topic, obj);
        log.info(obj);
    }
}