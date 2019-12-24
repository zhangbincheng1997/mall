package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum SexEnum implements CodeEnum {

    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女");

    private Integer code;
    private String msg;

    SexEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
