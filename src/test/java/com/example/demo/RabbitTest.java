package com.example.demo;

import com.example.demo.component.RabbitSender;
import com.example.demo.utils.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitTest {

    @Autowired
    private RabbitSender sender;

    @Test
    public void testDemo() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            sender.send(Constants.ORDER_TOPIC, LocalDateTime.now());
            TimeUnit.SECONDS.sleep(1);
        }
    }
}