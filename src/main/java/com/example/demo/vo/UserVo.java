package com.example.demo.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserVo {

//    @NotNull(message = "手机不能为空")
//    @Pattern(regexp = "1\\d{10}", message = "手机不符合规范")
//    private String mobile;

//    @NotNull(message = "邮箱不能为空")
//    @Email(message = "邮箱不符合规范")
//    private String email;

    @NotNull(message = "账号不能为空")
    @Size(min = 3, max = 12, message = "账号长度为3-12")
    private String username;

    @NotNull(message = "密码不能为空")
    @Size(min = 3, max = 12, message = "密码长度为3-12")
    private String password;
}
