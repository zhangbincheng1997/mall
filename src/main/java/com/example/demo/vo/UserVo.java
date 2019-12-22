package com.example.demo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserVo {

    private String email;

    private String avatar;

    private String nickname;

    private Integer gender;

    private Date birthday;

    private Date createTime;

    private Date loginTime;
}
