package cn.jnu.mbg.service.impl;

import cn.jnu.mbg.entity.Role;
import cn.jnu.mbg.mapper.RoleMapper;
import cn.jnu.mbg.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
