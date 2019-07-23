package com.example.demo.base;

import com.example.demo.entity.User;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitmqReceiver {

    @RabbitHandler
    @RabbitListener(queues = "string")
    public void process(String string) {
        System.out.println("Receiver  : " + string);
    }

    @RabbitHandler
    @RabbitListener(queues = "object")
    public void process(Object object) {
        System.out.println("Receiver  : " + object.toString());
    }

}
