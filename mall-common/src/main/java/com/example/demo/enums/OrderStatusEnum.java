package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum implements CodeEnum {

    TO_BE_PAID(0, "待付款"),
    TO_BE_SHIPPED(1, "待发货"),
    TO_BE_RECEIVED(2, "待收货"),
    CLOSE(3, "已关闭"),
    FINISH(4, "已完成"),
    REFUND_REQUEST(5, "退款申请"),
    REFUND_SUCCESS(6, "退款成功");

    private Integer code;
    private String msg;

    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
