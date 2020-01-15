package com.example.demo.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.demo.component.RedisService;
import com.example.demo.common.base.GlobalException;
import com.example.demo.common.base.Status;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.dto.RegisterDto;
import com.example.demo.dao.UserRolePermissionDao;
import com.example.demo.entity.Permission;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserRoleService;
import com.example.demo.service.UserService;
import com.example.demo.common.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserRolePermissionDao userRolePermissionDao;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .eq("username", username);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public void register(RegisterDto registerDto) {
        try {
            User user = new User()
                    .setUsername(registerDto.getUsername())
                    .setPassword(passwordEncoder.encode(registerDto.getPassword()));
            baseMapper.insert(user);
            UserRole userRole = new UserRole()
                    .setUserId(user.getId())
                    .setRoleId(2L); // 默认为ROLE_USER
            userRoleService.save(userRole);
        } catch (DuplicateKeyException e) {
            throw new GlobalException(Status.USERNAME_EXIST);
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

    @Override
    public List<String> getRoleList(Long id) {
        // user_id -> role_id
        // 用户表 -> 角色表
        return userRolePermissionDao.getRoleList(id).stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getPermissionList(Long id) {
        // user_id -> role_id -> permission_id
        // 用户表 -> 角色表 -> 权限表
        return userRolePermissionDao.getPermissionList(id).stream()
                .map(Permission::getName)
                .collect(Collectors.toList());
    }
}
