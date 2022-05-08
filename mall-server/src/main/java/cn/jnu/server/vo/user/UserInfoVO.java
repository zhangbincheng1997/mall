package cn.jnu.server.vo.user;

import cn.jnu.common.vo.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserInfoVO extends BaseVO {

    private String name;
    private String phone;
    private String email;
    private String avatar;
    private String company;
    private String department;
    private String description;

    // 当前登录用户拥有的角色
    private List<String> roles;

    // 当前登录用户拥有的按钮权限（接口权限不对外提供）
    private List<String> perms;
}
