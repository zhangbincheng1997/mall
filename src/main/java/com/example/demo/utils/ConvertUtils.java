package com.example.demo.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;

public class ConvertUtils {

    public static <T1, T2> T2 convert(T1 source, Class<T2> clazz) {
        try {
            final T2 target = clazz.newInstance(); // 获得泛型实例
            BeanUtil.copyProperties(source, target, CopyOptions.create().setIgnoreNullValue(true)); // 复制对象
            return target;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object convert(Object source, Object target) {
        BeanUtil.copyProperties(source, target, CopyOptions.create().setIgnoreNullValue(true)); // 复制对象
        return target;
    }
}
