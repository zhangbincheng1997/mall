package com.example.demo.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    /**
     * 根据key获取cookie
     *
     * @param request
     * @param key
     * @return
     */
    public static String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 设置cookie(key, value)
     *
     * @param response
     * @param key
     * @param value
     * @param expiry
     */
    public static void setCookie(HttpServletResponse response, String key, String value, int expiry) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiry);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
