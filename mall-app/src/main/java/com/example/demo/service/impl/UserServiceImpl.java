package com.example.demo.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.component.redis.RedisService;
import com.example.demo.enums.UserRoleEnum;
import com.example.demo.mapper.UserRoleMapper;
import com.example.demo.service.UserService;
import com.example.demo.utils.Constants;
import com.example.demo.dto.RegisterDto;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterDto registerDto) {
        try {
            User user = new User()
                    .setUsername(registerDto.getUsername())
                    .setPassword(passwordEncoder.encode(registerDto.getPassword()));
            baseMapper.insert(user);
            UserRole userRole = new UserRole()
                    .setUserId(user.getId())
                    .setRoleId(UserRoleEnum.USER.getCode().longValue());
            userRoleMapper.insert(userRole);
        } catch (DuplicateKeyException e) {
            throw new GlobalException(Status.USERNAME_EXIST); // FOREIGN KEY
        }
    }

    @Override
    public void updateUserInfoByUsername(String username, UserInfoDto userInfoDto) {
        User user = Convert.convert(User.class, userInfoDto);
        baseMapper.update(user, Wrappers.<User>lambdaUpdate()
                .eq(User::getUsername, username));
        redisService.delete(Constants.USER_KEY + username); // 刷新缓存
    }

    @Override
    public void updatePasswordByUsername(String username, String password) {
        User user = new User()
                .setPassword(passwordEncoder.encode(password));
        baseMapper.update(user, Wrappers.<User>lambdaUpdate()
                .eq(User::getUsername, username));
        redisService.delete(Constants.USER_KEY + username); // 刷新缓存
    }
}
