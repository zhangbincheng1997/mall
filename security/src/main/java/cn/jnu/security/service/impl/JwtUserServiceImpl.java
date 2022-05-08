package cn.jnu.security.service.impl;

import cn.jnu.mbg.entity.Role;
import cn.jnu.mbg.entity.User;
import cn.jnu.mbg.mapper.UserMapper;
import cn.jnu.security.dao.UserRolePermissionDao;
import cn.jnu.security.service.JwtUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JwtUserServiceImpl extends ServiceImpl<UserMapper, User> implements JwtUserService {

    private UserRolePermissionDao userRolePermissionDao;

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username);
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
