package com.example.demo.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UpdatePassVo {

    @NotNull(message = "旧密码不能为空")
    @Size(min = 3, max = 12, message = "旧密码长度为3-12")
    private String oldPass;

    @NotNull(message = "新密码不能为空")
    @Size(min = 3, max = 12, message = "新密码长度为3-12")
    private String newPass;
}
