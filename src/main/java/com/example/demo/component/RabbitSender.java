package com.example.demo.component;

import com.alibaba.fastjson.JSON;
import com.example.demo.base.AppLog;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String topic, String str) {
        this.rabbitTemplate.convertAndSend(topic, str);
        AppLog.info(str);
    }

    public void send(String topic, Object obj) {
        String jsonObj = JSON.toJSONString(obj);
        this.rabbitTemplate.convertAndSend(topic, jsonObj);
        AppLog.info(jsonObj);
    }
}
