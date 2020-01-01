package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum implements CodeEnum {

    NEW(0, "新订单"), // 默认
    BUYER_REQUEST_REFUND(1, "买家申请退款"),
    BUYER_REFUND_SUCCESS(2, "买家申请退款成功"),
    SELLER_REFUND_SUCCESS(3, "卖家主动退款成功"),
    CLOSED(4, "订单关闭"),
    FINISH(5, "订单完成");

    private Integer code;
    private String msg;

    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
