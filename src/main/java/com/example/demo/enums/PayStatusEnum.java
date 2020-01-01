package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum PayStatusEnum implements CodeEnum {

    FALSE(0, "未付款"), //  默认
    TRUE(1, "已付款"),
    REFUND(2, "已退款");

    private Integer code;
    private String msg;

    PayStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
