package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.vo.UserInfoVo;
import com.example.demo.vo.UserVo;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface UserService {

    User getUserByUsername(String username);

    User register(UserVo userVo);

    String login(UserVo userVo);

    void logout();

    int updatePasswordByEmail(UserVo userVo);

    int updateUserInfoByUsername(String username, UserInfoVo userInfoVo);

    List<GrantedAuthority> getRoleList(Long id);

    List<GrantedAuthority> getPermissionList(Long id);
}
