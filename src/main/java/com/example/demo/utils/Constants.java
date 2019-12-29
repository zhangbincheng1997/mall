package com.example.demo.utils;

public class Constants {

    public static final int REDIS_CACHE_EXPIRE = 60 * 60; // Redis Cache 缓存过期时间 60*60s

    public static final String ACCESS_KEY = "access:"; // 访问限制
    public static final String USER_KEY = "user:"; // 用户缓存
    public static final int USER_EXPIRE = 60 * 60; // 用户缓存过期时间 60*60s
    public static final String ROLE_KEY = "role:"; // 角色缓存
    public static final int ROLE_EXPIRE = 60 * 60; // 角色缓存过期时间 60*60s
    public static final String PERMISSION_KEY = "permission:"; // 权限缓存
    public static final int PERMISSION_EXPIRE = 60 * 60; // 权限缓存过期时间 60*60s

    public static final String TEST_TOPIC = "测试主题";

    public static final int UPLOAD_RETRY = 3; // 重传次数

    public static final int CODE_WIDTH = 120; // 验证码宽度
    public static final int CODE_HEIGHT = 38; // 验证码高度
    public static final int CODE_LENGTH = 4; //验证码字符长度
    public static final int CODE_EXPIRE = 60; // 验证码过期时间 60s
}
