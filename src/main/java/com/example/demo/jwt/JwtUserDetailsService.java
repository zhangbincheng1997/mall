package com.example.demo.jwt;

import com.alibaba.fastjson.JSON;
import com.example.demo.component.RedisService;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@Slf4j
@Configuration
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO 其他地方可能改变
        // 从redis取用户
        User user = (User) redisService.get(Constants.USER_KEY + username); // get
        if (user == null) {
            // 从mysql取
            user = userService.getUserByUsername(username);
            if (user != null) {
                log.info("缓存用户：" + JSON.toJSONString(user));
                redisService.set(Constants.USER_KEY + username, user, Constants.USER_EXPIRE); // set
            } else {
                throw new UsernameNotFoundException("用户名不存在");
            }
        }
        // 从redis取角色
        List<String> roleList = (List<String>) redisService.get(Constants.ROLE_KEY + username); // get
        if (roleList == null) {
            // 从mysql取
            roleList = userService.getRoleList(user.getId());
            log.info("缓存角色：" + roleList.toString());
            redisService.set(Constants.ROLE_KEY + username, roleList, Constants.ROLE_EXPIRE); // set
        }
        // 从redis取权限
        List<String> permissionList = (List<String>) redisService.get(Constants.PERMISSION_KEY + username); // get
        if (permissionList == null) {
            // 从mysql取
            permissionList = userService.getPermissionList(user.getId());
            log.info("缓存权限：" + permissionList.toString());
            redisService.set(Constants.PERMISSION_KEY + username, permissionList, Constants.PERMISSION_EXPIRE); // set
        }
        JwtUserDetails userDetails = new JwtUserDetails(user, roleList, permissionList);
        log.info("认证成功：" + JSON.toJSONString(userDetails));
        return userDetails;
    }
}
