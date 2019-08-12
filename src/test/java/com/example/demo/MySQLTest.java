//package com.example.demo;
//
//import com.example.demo.entity.User;
//import com.example.demo.repository.UserRepository;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class MySQLTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    public void testMySQL() {
//        User user = new User();
//        user.setAccount("account");
//        user.setPassword("password");
//        user.setNickname("nickname");
//
//        userRepository.save(user);
//        Assert.assertEquals("nickname", userRepository.findById(1L).get().getNickname());
//    }
//}
