package cn.jnu.mbg.service.impl;

import cn.jnu.mbg.entity.Permission;
import cn.jnu.mbg.mapper.PermissionMapper;
import cn.jnu.mbg.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

}
