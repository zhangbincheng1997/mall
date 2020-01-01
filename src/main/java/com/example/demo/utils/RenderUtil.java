package com.example.demo.utils;

import com.alibaba.fastjson.JSON;
import com.example.demo.base.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class RenderUtil {

    public static <T> void render(HttpServletResponse response, Result<T> result) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = cors(response).getOutputStream();
        String str = JSON.toJSONString(result);
        out.write(str.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }

    public static void render(HttpServletResponse response, String content) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        OutputStream out = cors(response).getOutputStream();
        out.write(content.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }

    // 允许跨域
    private static HttpServletResponse cors(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Header", "*");
        response.addHeader("Access-Control-Allow-Method", "*");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Max-Age", "86400");
        return response;
    }
}
