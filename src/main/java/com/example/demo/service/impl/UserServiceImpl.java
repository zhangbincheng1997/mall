package com.example.demo.service.impl;

import com.example.demo.base.GlobalException;
import com.example.demo.base.Status;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.UserRoleMapper;
import com.example.demo.mapper.UserRolePermissionMapper;
import com.example.demo.model.*;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import com.example.demo.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    public User register(UserDto userDto) {
        if (getUserByUsername(userDto.getUsername()) != null) {
            throw new GlobalException(Status.USERNAME_EXIST);
        } else {
            User user = new User();
            user.setUsername(userDto.getUsername());
            user.setPassword(passwordEncoder.encode(userDto.getPassword())); // JWT加密
            userMapper.insertSelective(user);
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(2L);
            userRoleMapper.insert(userRole);
            return user;
        }
    }

    @Override
    public void updateUserInfoByUsername(String username, UserInfoDto userInfoDto) {
        User user = ConvertUtils.convert(userInfoDto, User.class);
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        userMapper.updateByExampleSelective(user, example);
    }

    @Override
    public void updatePasswordByUsername(String username, String password) {
        User user = new User();
        user.setPassword(passwordEncoder.encode(password));
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        userMapper.updateByExampleSelective(user, example);
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
