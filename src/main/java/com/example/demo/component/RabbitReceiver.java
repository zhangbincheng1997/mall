package com.example.demo.component;

import com.example.demo.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitReceiver {

    @RabbitHandler
    @RabbitListener(queues = Constants.TEST_TOPIC)
    public void testProcess(String message) {
        log.info(message);
    }

}
