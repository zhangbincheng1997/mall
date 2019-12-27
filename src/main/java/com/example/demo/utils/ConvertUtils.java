package com.example.demo.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;

public class ConvertUtils {

    public static <T> T convert(Object source, Class<T> clazz) {
        try {
            final T target = clazz.newInstance(); // 获得泛型实例
            BeanUtil.copyProperties(source, target, CopyOptions.create().setIgnoreNullValue(true)); // 复制对象
            return target;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T convert(Object source, T target) {
        BeanUtil.copyProperties(source, target, CopyOptions.create().setIgnoreNullValue(true)); // 复制对象
        return target;
    }
}
