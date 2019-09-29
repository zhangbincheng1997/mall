package com.example.demo.mapper;

import com.example.demo.model.Permission;
import com.example.demo.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义映射
 */
public interface UserRolePermissionMapper {

    /**
     * 获取用户所有角色
     */
    List<Role> getRoleList(@Param("userId") Long userId);

    /**
     * 获取用户所有权限
     */
    List<Permission> getPermissionList(@Param("userId") Long userId);
}
