package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.RegisterDto;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.entity.User;

import java.util.List;

public interface UserService extends IService<User> {

    User getUserByUsername(String username);

    void register(RegisterDto registerDto);

    void updateUserInfoByUsername(String username, UserInfoDto userInfoDto);

    void updatePasswordByUsername(String username, String password);

}
