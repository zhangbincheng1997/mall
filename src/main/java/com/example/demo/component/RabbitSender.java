package com.example.demo.component;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component
public class RabbitSender {

    private Logger logger = LoggerFactory.getLogger(RabbitSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(Object obj) {
        String jsonObj = JSON.toJSONString(obj);
        this.rabbitTemplate.convertAndSend("message", jsonObj);
        logger.info("Rabbit Sender : " + jsonObj);
    }
}
