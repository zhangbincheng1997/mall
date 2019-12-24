package com.example.demo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UserDto {

    @ApiModelProperty(value = "用户名", required = true)
    @NotEmpty(message = "用户名不能为空")
    @Size(min = 3, max = 12, message = "用户名长度为3-12")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @NotEmpty(message = "密码不能为空")
    @Size(min = 3, max = 12, message = "密码长度为3-12")
    private String password;

    @ApiModelProperty(value = "验证码")
//    @Size(min = Constants.CODE_LENGTH, max = Constants.CODE_LENGTH, message = "验证码不符合规范")
    private String code;
}
