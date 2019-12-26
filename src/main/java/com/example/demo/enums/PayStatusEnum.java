package com.example.demo.enums;

public enum PayStatusEnum implements CodeEnum {

//    https://docs.open.alipay.com/270/105902/
//    WAIT_BUYER_PAY 交易创建，等待买家付款
//    RADE_CLOSED 未付款交易超时关闭，或支付完成后全额退款
//    TRADE_SUCCESS 交易支付成功
//    TRADE_FINISHED 交易结束，不可退款


    NEW(0, "交易创建"),
    CLOSED(1, "交易关闭"),
    SUCCESS(2, "交易成功"),
    FINISHED(3, "交易结束");

    @Override
    public Integer getCode() {
        return code;
    }

    private Integer code;
    private String msg;

    PayStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
