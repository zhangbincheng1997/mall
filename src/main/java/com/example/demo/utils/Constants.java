package com.example.demo.utils;

public class Constants {

    public static final int REDIS_CACHE_EXPIRE = 60 * 60; // Redis Cache 缓存过期时间 60*60s

    public static final String REDIS_PRODUCT_REDIS_LOCK = "product:redis:lock:"; // 商品加锁redis
    public static final String REDIS_PRODUCT_MYSQL_LOCK = "product:mysql:lock:"; // 商品加锁mysql
    public static final int REDIS_LOCK_LEASE_TIME = 10; // Redis Lock 缓存过期时间 10s
    public static final String REDIS_PRODUCT_POLLING = "product:polling:"; // 商品轮询
    public static final String REDIS_PRODUCT_INFO = "product:info:"; // 商品信息
    public static final String REDIS_PRODUCT_STOCK = "product:stock:"; // 商品库存

    public static final String ACCESS_KEY = "access:"; // 访问限制
    public static final String USER_KEY = "user:"; // 用户缓存
    public static final int USER_EXPIRE = 60 * 60; // 用户缓存过期时间 60*60s
    public static final String ROLE_KEY = "role:"; // 角色缓存
    public static final int ROLE_EXPIRE = 60 * 60; // 角色缓存过期时间 60*60s
    public static final String PERMISSION_KEY = "permission:"; // 权限缓存
    public static final int PERMISSION_EXPIRE = 60 * 60; // 权限缓存过期时间 60*60s

    public static final String ORDER_TOPIC = "订单主题";

    public static final int UPLOAD_RETRY = 3; // 重传次数

    public static final int CODE_WIDTH = 120; // 验证码宽度
    public static final int CODE_HEIGHT = 38; // 验证码高度
    public static final int CODE_LENGTH = 4; //验证码字符长度
    public static final int CODE_EXPIRE = 60; // 验证码过期时间 60s

    public static final String PAY_PRODUCT_CODE = "FAST_INSTANT_TRADE_PAY";
    public static final String PAY_SUBJECT = "PC支付测试";
    public static final String PAY_BODY = "支付宝PC支付测试";
    public static final String PAY_SUCCESS_RETURN = "<html>" +
            "<head>" +
            "<meta charset=\"utf-8\">" +
            "</head>" +
            "<body onload='alert(\"支付成功\");window.history.go(-3);'>" +
            "</body>" +
            "</html>"; // 回退历史-3
    public static final String PAY_FAILURE_RETURN = "<html>" +
            "<head>" +
            "<meta charset=\"utf-8\">" +
            "</head>" +
            "<body onload='alert(\"支付失败\");window.history.go(-3);'>" +
            "</body>" +
            "</html>"; // 回退历史-3
}
