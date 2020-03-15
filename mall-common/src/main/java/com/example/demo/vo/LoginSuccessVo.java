package com.example.demo.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LoginSuccessVo extends UserInfoVo {

    private String token;
}
