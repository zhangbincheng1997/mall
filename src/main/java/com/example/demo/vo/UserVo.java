package com.example.demo.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserVo {

//    @NotNull
//    @Pattern(regexp = "1\\d{10}") // 手机号码
//    private String mobile;

    @NotNull(message = "邮箱不能为空")
    @Email(message = "邮箱不符合规范") // 邮箱
    private String email;

    @NotNull
    @Size(min = 3, max = 12)
    private String password;
}
