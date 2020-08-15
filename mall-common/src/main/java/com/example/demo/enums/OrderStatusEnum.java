package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum implements CodeEnum {

    TO_BE_PAID(0, "待付款"),
    TO_BE_SHIPPED(1, "待发货"),
    TO_BE_RECEIVED(2, "待收货"),
    USER_CLOSE(3, "USER关闭"),
    GM_CLOSE(4, "GM关闭"),
    CANCEL(5, "订单取消"),
    FINISH(6, "订单完成"),
    REFUND_REQUEST(7, "退款申请"),
    REFUND_SUCCESS(8, "退款成功");

    private Integer code;
    private String msg;

    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
