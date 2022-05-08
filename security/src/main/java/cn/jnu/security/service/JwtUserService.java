package cn.jnu.security.service;

import cn.jnu.mbg.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface JwtUserService extends IService<User> {

    User getUserByUsername(String username);

    List<String> getRoleList(Long id);

    List<String> getPermissionList(Long id);
}
