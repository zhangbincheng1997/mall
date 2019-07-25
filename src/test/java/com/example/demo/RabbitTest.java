package com.example.demo;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.component.RabbitSender;
import com.example.demo.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitTest {

    @Autowired
    private RabbitSender sender;

    @Test
    public void testRabbitStr() {
        String str = new Date().toString();
        sender.send(str);
    }

    @Test
    public void testRabbitObj() {
        User user = new User();
        user.setAccount("account");
        user.setPassword("password");
        user.setNickname("nickname");
        sender.send(user);
    }
}
