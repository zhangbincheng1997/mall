package com.example.demo.utils;

import org.springframework.util.DigestUtils;

import java.util.UUID;

public class MD5Util {

    // 用户端：MD5（明文+固定salt）
    // 服务端：MD5（用户输入+随机salt）

    public static String MD5(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

    public static String MD5Salt(String str, String salt) {
        return MD5(str + salt);
    }

    public static String UUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void main(String[] args) {
        System.out.println(MD5("zzz"));
        System.out.println(MD5Salt("zzz", "xmu"));
        System.out.println(UUID());
    }
}
