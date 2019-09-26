package com.example.demo.utils;

import com.alibaba.fastjson.JSON;
import com.example.demo.base.Result;
import com.example.demo.base.Status;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class RenderUtils {

    public static void render(HttpServletResponse response, Result result) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(result);
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
