package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .eq("username", username);
        return baseMapper.selectOne(wrapper);
    }

}
