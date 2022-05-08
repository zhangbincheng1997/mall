package cn.jnu.mbg.service.impl;

import cn.jnu.mbg.entity.UserRole;
import cn.jnu.mbg.mapper.UserRoleMapper;
import cn.jnu.mbg.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
