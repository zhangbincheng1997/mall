package cn.jnu.security.dao;

import cn.jnu.mbg.entity.Permission;
import cn.jnu.mbg.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 自定义映射
 */
public interface UserRolePermissionDao {

    /**
     * 获取用户所有角色
     */
    @Select("SELECT r.*" +
            " FROM user u" +
            " LEFT JOIN user_role ur ON u.id = ur.user_id" +
            " LEFT JOIN role r ON ur.role_id = r.id" +
            " WHERE u.id=#{userId}")
    List<Role> getRoleList(@Param("userId") Long userId);

    /**
     * 获取用户所有权限
     */
    @Select("SELECT p.*" +
            " FROM user u" +
            " LEFT JOIN user_role ur ON u.id = ur.user_id" +
            " LEFT JOIN role r ON ur.role_id = r.id" +
            " LEFT JOIN role_permission rp ON rp.role_id = r.id" +
            " LEFT JOIN permission p ON p.id = rp.permission_id" +
            " WHERE u.id=#{userId}")
    List<Permission> getPermissionList(@Param("userId") Long userId);
}
