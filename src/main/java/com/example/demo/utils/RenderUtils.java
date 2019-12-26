package com.example.demo.utils;

import com.alibaba.fastjson.JSON;
import com.example.demo.base.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class RenderUtils {

    public static void render(HttpServletResponse response, Result result) throws IOException {
        // 允许跨域
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Header", "*");
        response.addHeader("Access-Control-Allow-Method", "*");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Max-Age", "86400");
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(result);
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
