package com.example.demo.base;

import com.example.demo.entity.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitmqSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(String string) {
        System.out.println("Sender : " + string);
        this.rabbitTemplate.convertAndSend("string", string);
    }

    public void send(Object object) {
        System.out.println("Sender : " + object.toString());
        this.rabbitTemplate.convertAndSend("object", object);
    }

}
