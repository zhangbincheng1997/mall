package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface UserService {

    User getUserByUsername(String username);

    User register(UserDto userDto);

    int updateUserInfoByUsername(String username, UserInfoDto userInfoDto);

    int updatePasswordByUsername(String username, String password);

    int updateAvatarByUsername(String username, String avatar);

    List<GrantedAuthority> getRoleList(Long id);

    List<GrantedAuthority> getPermissionList(Long id);
}
