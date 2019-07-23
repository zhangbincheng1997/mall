package com.example.demo;

import com.example.demo.base.RabbitmqSender;
import com.example.demo.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqTest {

    @Autowired
    private RabbitmqSender rbSender;

    @Test
    public void testRabbitmq() {
        String str = "hello " + new Date();
        rbSender.send(str);
    }

    @Test
    public void testRabbitmqObj() {
        User user = new User();
        user.setAccount("account");
        user.setPassword("password");
        user.setNickname("nickname");
        rbSender.send(user);
    }
}
