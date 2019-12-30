package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.dto.RegisterDto;

import java.util.List;

public interface UserService {

    User getUserByUsername(String username);

    int register(RegisterDto registerDto);

    int updateUserInfoByUsername(String username, UserInfoDto userInfoDto);

    int updatePasswordByUsername(String username, String password);

    List<String> getRoleList(Long id);

    List<String> getPermissionList(Long id);
}
