package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.User;

import java.util.List;

public interface JwtUserService extends IService<User> {

    User getUserByUsername(String username);

    List<String> getRoleList(Long id);

    List<String> getPermissionList(Long id);
}
