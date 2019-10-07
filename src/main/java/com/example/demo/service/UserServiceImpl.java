package com.example.demo.service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.UserRolePermissionMapper;
import com.example.demo.model.*;
import com.example.demo.vo.UserInfoVo;
import com.example.demo.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRolePermissionMapper userRolePermissionMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUserByUsername(String username) {
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<User> userList = userMapper.selectByExample(example);
        if (userList != null && userList.size() > 0) {
            return userList.get(0);
        }
        return null;
    }

    @Override
    public User register(UserVo userVo) {
        User user = getUserByUsername(userVo.getUsername());
        if (user != null) {
            return null;
        }
        user = new User();
        user.setUsername(userVo.getUsername());
        user.setPassword(passwordEncoder.encode(userVo.getPassword()));
        user.setCreateTime(new Date());
        userMapper.insert(user);
        return user;
    }

    @Override
    public int updatePasswordByEmail(UserVo userVo) {
        UserExample example = new UserExample();
        example.createCriteria().andEmailEqualTo(userVo.getEmail());
        List<User> userList = userMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(userList)) {
            return 0;
        }
        User user = userList.get(0);
        user.setPassword(passwordEncoder.encode(userVo.getPassword()));
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int updateUserInfoByUsername(String username, UserInfoVo userInfoVo) {
        User user = new User();
        BeanUtils.copyProperties(userInfoVo, user); // source -> target 忽略空值
        UserExample example = new UserExample();
        example.createCriteria().andUsernameEqualTo(username);
        return userMapper.updateByExampleSelective(user, example);
    }

    @Override
    public List<GrantedAuthority> getRoleList(Long id) {
        // user_id -> role_id
        // 用户表 -> 角色表
        List<Role> roleList = userRolePermissionMapper.getRoleList(id);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Role role : roleList) {
            if (role != null && role.getName() != null) {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName());
                grantedAuthorities.add(grantedAuthority);
            }
        }
        // 流式操作
//        grantedAuthorities = roleList.stream()
//                .filter(role -> role.getName() != null)
//                .map(role -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toList());
        return grantedAuthorities;
    }

    @Override
    public List<GrantedAuthority> getPermissionList(Long id) {
        // user_id -> role_id -> permission_id
        // 用户表 -> 角色表 -> 权限表
        List<Permission> permissionList = userRolePermissionMapper.getPermissionList(id);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Permission permission : permissionList) {
            if (permission != null && permission.getName() != null) {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getName());
                grantedAuthorities.add(grantedAuthority);
            }
        }
        // 流式操作
//        grantedAuthorities = permissionList.stream()
//                .filter(permission -> permission.getName() != null)
//                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
//                .collect(Collectors.toList());
        return grantedAuthorities;
    }
}
