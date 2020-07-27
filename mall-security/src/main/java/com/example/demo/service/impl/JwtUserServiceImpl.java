package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.UserRolePermissionDao;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.JwtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtUserServiceImpl extends ServiceImpl<UserMapper, User> implements JwtUserService {

    @Autowired
    private UserRolePermissionDao userRolePermissionDao;

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .eq("username", username);
        return baseMapper.selectOne(wrapper);
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
//        return userRolePermissionDao.getPermissionList(id).stream()
//                .map(Permission::getName)
//                .collect(Collectors.toList());
        return new ArrayList<>();
    }
}
