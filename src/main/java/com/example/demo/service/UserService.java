package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.dto.RegisterDto;

import java.util.List;

public interface UserService {

    User getUserByUsername(String username);

    User register(RegisterDto registerDto);

    void updateUserInfoByUsername(String username, UserInfoDto userInfoDto);

    void updatePasswordByUsername(String username, String password);

    List<String> getRoleList(Long id);

    List<String> getPermissionList(Long id);
}
