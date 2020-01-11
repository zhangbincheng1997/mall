package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum implements CodeEnum {

    NEW(0, "新订单"), //  默认
    PAY(1, "已付款"),
    ORDER(2, "已接单"),
    SHIP(3, "已发货"),
    CANCEL(4, "已取消"),
    FINISH(5, "已完成"),
    REFUND_REQUEST(6, "退款申请"),
    REFUND_SUCCESS(7, "退款成功");

    private Integer code;
    private String msg;

    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
