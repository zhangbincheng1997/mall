package com.example.demo;

import com.example.demo.mapper.UserRolePermissionMapper;
import com.example.demo.model.Permission;
import com.example.demo.model.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoTest {

    @Autowired
    private UserRolePermissionMapper userRolePermissionMapper;

    @Test
    public void testDemo() {
        List<Role> roleList = userRolePermissionMapper.getRoleList(1L);
        System.out.println(roleList);
        List<Permission> permissionList = userRolePermissionMapper.getPermissionList(1L);
        System.out.println(permissionList);
    }
}
