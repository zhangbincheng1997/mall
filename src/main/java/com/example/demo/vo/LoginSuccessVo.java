package com.example.demo.vo;

import lombok.Data;

@Data
public class LoginSuccessVo {

    private String token;

    private String avatar = "http://qiniu.littleredhat1997.com/avatar.png";

    private String nickname = "zzz";
}
