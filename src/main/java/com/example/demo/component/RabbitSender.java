package com.example.demo.component;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String topic, String str) {
        this.rabbitTemplate.convertAndSend(topic, str);
        log.info(str);
    }

    public void send(String topic, Object obj) {
        String jsonObj = JSON.toJSONString(obj);
        this.rabbitTemplate.convertAndSend(topic, jsonObj);
        log.info(jsonObj);
    }
}
