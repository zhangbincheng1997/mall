package com.example.demo.utils;

import java.util.UUID;

public class UUIDUtils {

    public static String UUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void main(String[] args) {
        System.out.println(UUID());
    }
}

