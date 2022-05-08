package cn.jnu.server.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class UserAddDTO {

    @ApiModelProperty(value = "账号")
    @NotEmpty(message = "账号不能为空")
    private String username;

    @ApiModelProperty(value = "密码")
    @NotEmpty(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "姓名")
    @NotEmpty(message = "姓名不能为空")
    private String name;

    @ApiModelProperty(value = "手机")
    @NotEmpty(message = "手机不能为空")
    @Pattern(regexp = "^1[3456789]\\d{9}$", message = "手机不符合Phone规范")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    @NotEmpty(message = "邮箱不能为空")
    @Email(message = "邮箱不符合Email规范")
    private String email;

    @ApiModelProperty(value = "公司名称")
    private String company;

    @ApiModelProperty(value = "部门名称")
    private String department;

    @ApiModelProperty(value = "用户描述")
    private String description;

    @ApiModelProperty(value = "角色id列表")
    private List<Long> roleIds;
}
