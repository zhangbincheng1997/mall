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
    CONSTRAINT_VIOLATION_EXCEPTION(1001, "参数错误2"),

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
    ACCESS_LIMIT(10000, "您已超出访问限制"),

    // 验证码
    CAPTCHA_EXPIRE(20000, "验证码过期"),
    CAPTCHA_ERROR(20001, "验证码错误"),

    // 上传文件
    FILE_UPLOAD_EMPTY(30000, "传了个空文件"),
    FILE_UPLOAD_ERROR(30001, "咋回事，文件有毒？"),

    // 账号相关
    USERNAME_EXIST(50010, "账号已经存在"),
    USERNAME_NOT_EXIST(50011, "账号不存在"),

    // 商品相关
    PRODUCT_NOT_EXIST(60000, "商品不存在"),
    PRODUCT_STATUS_NOT_ON(60001, "商品下架"),
    PRODUCT_STOCK_NOT_ENOUGH(60002, "商品库存不足"),
    CATEGORY_EXIST(60010, "分类已经存在"),
    CATEGORY_NOT_EXIST(60011, "分类不存在"),
    CATEGORY_NOT_DELETE(60012, "分类不允许删除"),
    ORDER_NOT_EXIST(60020, "订单不存在");

    private Integer code;
    private String msg;

    Status(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
