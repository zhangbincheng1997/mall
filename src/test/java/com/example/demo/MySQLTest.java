package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.utils.MD5Utils;
import com.example.demo.utils.UUIDUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MySQLTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testMySQL() {
        User user = new User();
        user.setEmail("test");
        String salt = UUIDUtils.UUID();
        String password = MD5Utils.MD5Salt("password", salt);
        user.setPassword(password);
        user.setSalt(salt);
        System.out.println(user);

        userMapper.insert(user);
        Assert.assertEquals(salt, userMapper.getByEmail(user.getEmail()).getSalt());
    }
}
