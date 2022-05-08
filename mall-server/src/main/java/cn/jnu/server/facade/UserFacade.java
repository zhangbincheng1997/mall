package cn.jnu.server.facade;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.jnu.common.base.GlobalException;
import cn.jnu.common.base.ResultCode;
import cn.jnu.mbg.entity.User;
import cn.jnu.mbg.entity.UserRole;
import cn.jnu.mbg.service.UserRoleService;
import cn.jnu.mbg.service.UserService;
import cn.jnu.server.dto.user.UserPageQuery;
import cn.jnu.server.dto.user.UserEnabledDTO;
import cn.jnu.server.dto.user.UserInfoDTO;
import cn.jnu.server.mapper.UserPageMapper;
import cn.jnu.server.vo.user.UserInfoVO;
import cn.jnu.server.vo.user.UserRecordVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class UserFacade {

    private final PasswordEncoder passwordEncoder;
    private final UserPageMapper userPageMapper;
    private final UserRoleService userRoleService;
    private final UserService userService;

    public User getById(Long id) {
        return userService.getById(id);
    }

    public User getByUsername(String username) {
        return userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    public Page<UserRecordVO> list(UserPageQuery query) {
        Page<UserInfoVO> page = new Page<>(query.getPage(), query.getLimit());
        return userPageMapper.page(page, query); // 联表查询
    }

    public void save(List<Long> roleIds, User user) {
        // 保存账号密码
        String username = user.getUsername();
        String password = user.getPassword();
        if (getByUsername(username) != null) {
            throw new GlobalException(ResultCode.USERNAME_EXIST);
        }
        user.setPassword(passwordEncoder.encode(password));
        userService.save(user);
        // 设置用户角色列表
        if (CollectionUtil.isNotEmpty(roleIds)) {
            List<UserRole> list = roleIds.stream()
                    .map(roleId -> new UserRole().setUserId(user.getId()).setRoleId(roleId))
                    .collect(Collectors.toList());
            userRoleService.saveBatch(list);
        }
    }

    public void updateInfo(Long id, UserInfoDTO DTO) {
        User user = Convert.convert(User.class, DTO).setId(id);
        userService.updateById(user);
    }

    public void updatePassword(Long id, String password) {
        User user = new User().setId(id).setPassword(passwordEncoder.encode(password));
        userService.updateById(user);
    }

    public void updateStatus(UserEnabledDTO DTO) {
        List<User> userList = new ArrayList<>();
        for (Long userId : DTO.getUserIds()) {
            User user = new User();
            user.setId(userId);
            user.setStatus(DTO.getStatus());
            userList.add(user);
        }
        userService.updateBatchById(userList);
    }

    public void delete(List<Long> idList) {
        userService.removeByIds(idList);
        userRoleService.remove(Wrappers.<UserRole>lambdaQuery().in(UserRole::getUserId, idList));
    }
}
