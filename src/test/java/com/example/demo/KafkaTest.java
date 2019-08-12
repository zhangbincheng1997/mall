//package com.example.demo;
//
//import com.example.demo.component.KafkaSender;
//import com.example.demo.entity.User;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Date;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class KafkaTest {
//
//    @Autowired
//    private KafkaSender sender;
//
//    @Test
//    public void testKafkaStr() {
//        String str = new Date().toString();
//        sender.send(str);
//    }
//
//    @Test
//    public void testKafkaObj() {
//        User user = new User();
//        user.setAccount("account");
//        user.setPassword("password");
//        user.setNickname("nickname");
//        sender.send(user);
//    }
//}
