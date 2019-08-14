package com.example.demo.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    // get cookie token
    private String getCookieToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(Constants.COOKIE_TOKEN)) {
                return cookie.getValue();
            }
        }
        return "";
    }

    // set cookie token
    private void setCookieToken(HttpServletResponse response, String token, int expiry) {
        Cookie cookie = new Cookie(Constants.COOKIE_TOKEN, token);
        cookie.setMaxAge(expiry);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
