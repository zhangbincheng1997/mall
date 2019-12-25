package com.example.demo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class LoginVo {

    private String token;

    private String avatar = "http://qiniu.littleredhat1997.com/avatar.png";

    private String nickname = "zzz";
}
