package cn.jnu.server;

import cn.jnu.common.enums.UserRoleEnum;
import cn.jnu.mbg.entity.User;
import cn.jnu.mbg.entity.UserRole;
import cn.jnu.mbg.service.UserRoleService;
import cn.jnu.mbg.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AdminTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void generateAdmin() {
        User user = new User()
                .setUsername("admin")
                .setPassword(passwordEncoder.encode("admin"))
                .setAvatar("http://qiniu.littleredhat1997.com/FvjJNyWw7hmQFVsp7jm86nDm9Ex_")
                .setName("管理员")
                .setPhone("15521106350")
                .setEmail("1656704949@qq.com");
        userService.save(user);

        UserRole userRole = new UserRole()
                .setUserId(user.getId())
                .setRoleId(UserRoleEnum.ADMIN.getCode().longValue());
        userRoleService.save(userRole);

        Assert.assertEquals("admin", userService.getById(user.getId()).getUsername());
        Assert.assertEquals(Long.valueOf(1), userRoleService.getById(userRole.getId()).getRoleId());
    }
}
