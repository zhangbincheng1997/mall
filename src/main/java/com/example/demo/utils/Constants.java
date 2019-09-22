package com.example.demo.utils;

public class Constants {

    /**
     * 图像验证码
     */
    public static final String CAPTCHA_KEY = "captcha";

    /**
     * 邮箱验证码
     */
    public static final String EMAIL_KEY = "email";


    /**
     * 验证码时间
     */
    public static final int EMAIL_EXPIRY = 60 * 3;

    /**
     * COOKIE标签
     */
    public static final String COOKIE_TOKEN = "token";

    /**
     * COOKIE过期时间
     */
    public static final int COOKIE_EXPIRY = 60 * 60 * 24;

    /**
     * 访问限制
     */
    public static final String ACCESS_KEY = "access";

    /**
     * RabbitMQ主题：用于开发测试
     */
    public static final String TEST_TOPIC = "测试主题";

    /**
     * RabbitMQ主题：用于发送邮件
     */
    public static final String EMAIL_TOPIC = "邮件主题";
}
