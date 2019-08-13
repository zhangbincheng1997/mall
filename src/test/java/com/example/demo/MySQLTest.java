package com.example.demo;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.MD5Utils;
import com.example.demo.utils.UUIDUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MySQLTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testMySQL() {
        User user = new User();
        user.setMobile("account");
        String salt = UUIDUtils.UUID();
        user.setPassword(MD5Utils.MD5Salt("password", salt));
        user.setSalt(salt);
        user.setRegisterDate(new Date());
        user.setLastLoginDate(new Date());
        System.out.println(user);

        userRepository.save(user);
        Assert.assertEquals("account", userRepository.findById(2L).get().getMobile());
    }
}
