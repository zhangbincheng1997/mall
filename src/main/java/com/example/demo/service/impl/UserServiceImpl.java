package com.example.demo.service.impl;

import cn.hutool.core.convert.Convert;
import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.UserRoleMapper;
import com.example.demo.mapper.UserRolePermissionMapper;
import com.example.demo.model.*;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.dto.RegisterDto;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserRolePermissionMapper userRolePermissionMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUserByUsername(String username) {
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<User> userList = userMapper.selectByExample(example);
        if (userList.size() == 0) {
            return null; // new GlobalException(Status.USERNAME_NOT_EXIST);
        } else {
            return userList.get(0);
        }
    }

    @Override
    public int register(RegisterDto registerDto) {
        try {
            User user = new User();
            user.setUsername(registerDto.getUsername());
            user.setPassword(passwordEncoder.encode(registerDto.getPassword())); // JWT加密
            userMapper.insertSelective(user);
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(2L); // 默认为ROLE_USER
            return userRoleMapper.insertSelective(userRole);
        } catch (DuplicateKeyException e) { // UNIQUE
            throw new GlobalException(Status.USERNAME_EXIST);
        }
    }

    @Override
    public int updateUserInfoByUsername(String username, UserInfoDto userInfoDto) {
        User user = Convert.convert(User.class, userInfoDto);
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        return userMapper.updateByExampleSelective(user, example);
    }

    @Override
    public int updatePasswordByUsername(String username, String password) {
        User user = new User();
        user.setPassword(passwordEncoder.encode(password));
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        return userMapper.updateByExampleSelective(user, example);
    }

    @Override
    public List<String> getRoleList(Long id) {
        // user_id -> role_id
        // 用户表 -> 角色表
        return userRolePermissionMapper.getRoleList(id)
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getPermissionList(Long id) {
        // user_id -> role_id -> permission_id
        // 用户表 -> 角色表 -> 权限表
        return userRolePermissionMapper.getPermissionList(id)
                .stream()
                .map(permission -> permission.getName())
                .collect(Collectors.toList());
    }
}
