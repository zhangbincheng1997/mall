//package com.example.demo;
//
//import com.example.demo.entity.User;
//import com.example.demo.entity.UserRole;
//import com.example.demo.enums.UserRoleEnum;
//import com.example.demo.mapper.UserMapper;
//import com.example.demo.mapper.UserRoleMapper;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Date;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class AdminTest {
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @Autowired
//    private UserRoleMapper userRoleMapper;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Test
//    public void generateAdmin() {
//        User user = new User()
//                .setUsername("admin")
//                .setPassword(passwordEncoder.encode("admin"))
//                .setAvatar("http://qiniu.littleredhat1997.com/FvjJNyWw7hmQFVsp7jm86nDm9Ex_")
//                .setNickname("超级用户")
//                .setEmail("1656704949@qq.com")
//                .setGender(0)
//                .setBirthday(new Date());
//        userMapper.insert(user);
//
//        UserRole userRole = new UserRole()
//                .setUserId(user.getId())
//                .setRoleId(UserRoleEnum.GM.getCode().longValue());
//        userRoleMapper.insert(userRole);
//
//        Assert.assertEquals("admin", userMapper.selectById(user.getId()).getUsername());
//        Assert.assertEquals(Long.valueOf(1L), userRoleMapper.selectById(userRole.getId()).getRoleId());
//    }
//}
