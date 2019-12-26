package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum implements CodeEnum {

    NEW(0, "新订单"),
    CANCEL(1, "已取消"),
    FINISHED(2, "已完成");

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;
    private String msg;

    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
