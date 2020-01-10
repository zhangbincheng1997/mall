package com.example.demo.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum implements CodeEnum {

    // 待付款 PENDING    buyer取消订单、立即付款 seller取消订单
    // 处理中 PROCESSED  buyer申请退款 seller立即发货
    // 已发货 SHIPPED    buyer确认收货
    // 已取消 CANCEL
    // 已完成 FINISH
    // 退款申请 REFUND_REQUEST
    // 退款成功 REFUND_SUCCESS

    PENDING(0, "待付款"), //  默认
    PROCESSED(1, "处理中"),
    SHIPPED(2, "已发货"),
    CANCEL(3, "已取消"),
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
