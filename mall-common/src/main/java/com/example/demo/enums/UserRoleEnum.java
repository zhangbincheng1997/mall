package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum UserRoleEnum implements CodeEnum {

    GM(1, "超级用户"),
    USER(2, "普通用户");

    private Integer code;
    private String msg;

    UserRoleEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
