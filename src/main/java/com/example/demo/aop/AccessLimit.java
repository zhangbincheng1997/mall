package com.example.demo.aop;

import com.example.demo.utils.Constants;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {
    boolean ip() default true; // true限制ip false不限制ip

    int time() default Constants.ACCESS_EXPIRE; // 单位:s

    int count();
}
