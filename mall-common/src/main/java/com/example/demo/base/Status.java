package com.example.demo.base;

import lombok.Getter;

@Getter
public enum Status {

    // 服务器
    SUCCESS(0, "SUCCESS"),
    FAILURE(250, "FAILURE"),
    FORBIDDEN(403, "拒绝执行"),
    NOT_FOUND(404, "请求失败"),
    BIND_EXCEPTION(1000, "参数错误"),

    // 认证权限
    ACCESS_DENIED(1001, "权限不足:拒绝访问"),
    AUTHENTICATION_ENTRY_POINT(1002, "权限不足：认证入口点"),
    AUTHENTICATION_FAILURE(1003, "认证失败"),
    USERNAME_NOT_FOUND(1004, "认证失败：用户名不存在"),
    BAD_CREDENTIALS(1005, "认证失败：密码错误"),
    ACCOUNT_DISABLED(1009, "认证失败：用户不可用"),
    ACCOUNT_EXPIRED(1006, "认证失败：用户过期"),
    ACCOUNT_LOCKED(1007, "认证失败：用户锁定"),
    CREDENTIALS_EXPIRED(1008, "认证失败：证书过期"),

    // 访问限制
    ACCESS_LIMIT(10000, "操作太频繁了，已超出访问限制！"),

    // 验证码
    CAPTCHA_EXPIRE(20000, "验证码过期"),
    CAPTCHA_ERROR(20001, "验证码错误"),

    // 上传文件
    FILE_UPLOAD_EMPTY(30000, "传了个空文件"),
    FILE_UPLOAD_ERROR(30001, "咋回事，文件有毒？"),

    // 账号相关
    USERNAME_EXIST(40000, "账号已经存在"),
    USERNAME_NOT_EXIST(40001, "账号不存在"),

    // 商品相关
    CART_EMPTY(50000, "购物车为空"),
    PRODUCT_NOT_EXIST(50010, "商品不存在"),
    PRODUCT_STOCK_NOT_ENOUGH(50011, "商品库存不足"),

    // 订单相关
    ORDER_NOT_EXIST(60000, "订单不存在"),
    ORDER_NOT_TO_BE_PAID(60001, "订单不属于待付款"),
    ORDER_NOT_TO_BE_SHIPPED(60002, "订单不属于待发货"),
    ORDER_NOT_TO_BE_RECEIVED(60003, "订单不属于待收货"),
    ORDER_CANCEL(60004, "订单已取消"),
    ORDER_FINISH(60005, "订单已完成"),
    ORDER_NOT_REFUND_REQUEST(60006, "订单没有退款申请"),
    ORDER_REFUND_SUCCESS(60007, "订单退款成功"),

    // 支付相关
    PAY_BUG(70000, "支付异常"),
    PAY_CHECK_BUG(70001, "支付检查异常"),
    REFUND_BUG(70002, "订单退款异常"),
    CLOSE_BUG(70003, "订单关闭异常"),
    CANCEL_BUG(70004, "订单撤销异常");

    private Integer code;
    private String msg;

    Status(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
