package com.example.demo.utils;

public class Constants {

    /**
     * 图像验证码
     * 邮箱验证码
     * COOKIE标签
     * 访问限制
     */
    public static final String CAPTCHA_KEY = "captcha";
    public static final String EMAIL_KEY = "email";
    public static final String COOKIE_TOKEN = "token";
    public static final String ACCESS_KEY = "access";

    /**
     * 验证码时间
     * COOKIE过期时间
     */
    public static final int EMAIL_EXPIRY = 60 * 3;
    public static final int COOKIE_EXPIRY = 60 * 60 * 24;



    /**
     * RabbitMQ主题：用于开发测试
     * RabbitMQ主题：用于发送邮件
     */
    public static final String TEST_TOPIC = "测试主题";
    public static final String EMAIL_TOPIC = "邮件主题";
}
