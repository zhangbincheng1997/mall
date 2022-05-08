package cn.jnu.server.vo.user;

import cn.jnu.common.vo.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserRecordVO extends BaseVO {

    private String name;
    private String phone;
    private String email;
    private String avatar;
    private String company;
    private String department;
    private String description;

    // 用户角色
    private List<RoleVO> roles;
}
