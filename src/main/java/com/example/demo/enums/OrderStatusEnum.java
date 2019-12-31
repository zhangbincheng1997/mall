package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum implements CodeEnum {

    NEW(0), // 新订单 默认
    BUYER_REQUEST_REFUND(1), // 买家申请退款
    BUYER_REFUND_SUCCESS(2), // 买家申请退款成功
    SELLER_REFUND_SUCCESS(3), // 卖家主动退款成功
    FINISH(4); // 订单完成

    private Integer code;

    OrderStatusEnum(Integer code) {
        this.code = code;
    }
}
