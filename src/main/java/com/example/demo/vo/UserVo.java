package com.example.demo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
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
}
