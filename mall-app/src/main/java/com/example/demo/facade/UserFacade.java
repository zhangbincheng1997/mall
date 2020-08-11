package com.example.demo.facade;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(RegisterDto registerDto) {
        try {
            User user = new User()
                    .setUsername(registerDto.getUsername())
                    .setPassword(passwordEncoder.encode(registerDto.getPassword()));
            userService.save(user);
            UserRole userRole = new UserRole()
                    .setUserId(user.getId())
                    .setRoleId(UserRoleEnum.USER.getCode().longValue());
            userRoleMapper.insert(userRole);
        } catch (DuplicateKeyException e) {
            throw new GlobalException(Status.USERNAME_EXIST); // FOREIGN KEY
        }
    }

    public void updateUserInfoByUsername(String username, UserInfoDto userInfoDto) {
        User user = Convert.convert(User.class, userInfoDto);
        userService.update(user, Wrappers.<User>lambdaUpdate()
                .eq(User::getUsername, username));
        redisService.delete(Constants.USER_KEY + username); // 刷新缓存
    }

    public void updatePasswordByUsername(String username, String password) {
        User user = new User()
                .setPassword(passwordEncoder.encode(password));
        userService.update(user, Wrappers.<User>lambdaUpdate()
                .eq(User::getUsername, username));
        redisService.delete(Constants.USER_KEY + username); // 刷新缓存
    }
}
