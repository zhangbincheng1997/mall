package cn.jnu.mbg.service.impl;

import cn.jnu.mbg.entity.User;
import cn.jnu.mbg.mapper.UserMapper;
import cn.jnu.mbg.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
