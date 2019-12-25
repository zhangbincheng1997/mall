package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.dto.UserDto;

import java.util.List;

public interface UserService {

    User getUserByUsername(String username);

    User register(UserDto userDto);

    int updateUserInfoByUsername(String username, UserInfoDto userInfoDto);

    int updatePasswordByUsername(String username, String password);

    int updateAvatarByUsername(String username, String avatar);

    List<String> getRoleList(Long id);

    List<String> getPermissionList(Long id);
}
