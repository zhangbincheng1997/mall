package com.example.demo.vo;

import com.example.demo.utils.Constants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserVo {

    @ApiModelProperty(value = "账号", required = true)
    @NotNull(message = "账号不能为空")
    @Size(min = 3, max = 12, message = "账号长度为3-12")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @NotNull(message = "密码不能为空")
    @Size(min = 3, max = 12, message = "密码长度为3-12")
    private String password;

    @ApiModelProperty(value = "手机")
    @Pattern(regexp = "1\\d{10}", message = "手机不符合规范")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱不符合规范")
    private String email;

    @ApiModelProperty(value = "验证码")
    @Size(min = Constants.CODE_LENGTH, max = Constants.CODE_LENGTH, message = "验证码不符合规范")
    private String code;
}
