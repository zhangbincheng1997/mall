package com.example.demo;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MySQLTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testMySQL() {
        User user = new User();
        user.setUsername("username");
        user.setPassword(passwordEncoder.encode("password"));

        System.out.println(user);
        userMapper.insert(user);
        System.out.println(user);

        Assert.assertEquals("username", userMapper.selectByPrimaryKey(user.getId()).getUsername());
    }
}
