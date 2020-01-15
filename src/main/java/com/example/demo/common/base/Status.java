package com.example.demo.common.base;

import lombok.Getter;

@Getter
public enum Status {

    // 服务器
    SUCCESS(0, "SUCCESS"),
    FAILURE(250, "FAILURE"),
    FORBIDDEN(403, "拒绝执行"),
    NOT_FOUND(404, "请求失败"),
    BIND_EXCEPTION(1000, "参数错误"),
    CONSTRAINT_VIOLATION_EXCEPTION(1001, "参数错误2"),
    HTTP_MESSAGE_NOT_READABLE_EXCEPTION(1002, "参数错误3"),

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
    USERNAME_EXIST(40010, "账号已经存在"),
    USERNAME_NOT_EXIST(40011, "账号不存在"),

    // 商品相关
    PRODUCT_NOT_EXIST(50000, "商品不存在"),
    PRODUCT_STATUS_NOT_ON(50001, "商品下架"),
    PRODUCT_STOCK_NOT_ENOUGH(50002, "商品库存不足"),
    CATEGORY_EXIST(50010, "分类已经存在"),
    CATEGORY_ROOT_NOT_EXIST(50012, "分类父类不存在"),
    CATEGORY_NOT_EXIST(50012, "分类不存在"),
    CATEGORY_NOT_DELETE(50013, "分类不允许删除"),
    ORDER_NOT_EXIST(50020, "订单不存在"),
    SKU_NOT_EXIST(50030, "SKU不存在"),

    // 支付相关
    PAY_BUG(60000, "支付异常"),
    PAY_CHECK_BUG(60001, "支付检查异常"),

    // 订单
    ORDER_NOT_TO_BE_PAID(70000, "订单不属于待付款"),
    ORDER_NOT_TO_BE_SHIPPED(70001, "订单不属于待发货"),
    ORDER_NOT_TO_BE_RECEIVED(70002, "订单不属于待收货"),
    ORDER_CANCEL(70003, "订单已取消"),
    ORDER_FINISH(70004, "订单已完成"),
    ORDER_NOT_REFUND_REQUEST(70006, "订单没有退款申请"),
    ORDER_REFUND_SUCCESS(70007, "订单退款成功");

    private Integer code;
    private String msg;

    Status(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
