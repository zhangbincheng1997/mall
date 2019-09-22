package com.example.demo.controller;

import com.example.demo.access.AccessLimit;
import com.example.demo.base.Result;
import com.example.demo.base.Status;
import com.example.demo.component.RedisService;
import com.example.demo.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Size;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

@Controller
@Validated
public class CaptchaController {

    @Autowired
    private RedisService redisService;

    private static final int CAPTCHA_WIDTH = 120;
    private static final int CAPTCHA_HEIGHT = 30;
    private static final int CAPTCHA_COUNT = 4;
    private static final int CAPTCHA_FONT_HEIGHT = 24;
    private static final int CAPTCHA_CODE_X = 20;
    private static final int CAPTCHA_CODE_Y = 20;

    private static final char[] codeSequence = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private void out(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 定义图像 Buffer
        BufferedImage buffImg = new BufferedImage(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, CAPTCHA_WIDTH, CAPTCHA_HEIGHT);
        // 设置字体
        g.setFont(new Font("Arial", Font.BOLD, CAPTCHA_FONT_HEIGHT));
        // 绘制干扰线
        Random random = new Random();
        g.setColor(Color.BLACK);
        for (int i = 0; i < 40; i++) {
            int x = random.nextInt(CAPTCHA_WIDTH);
            int y = random.nextInt(CAPTCHA_HEIGHT);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }
        // 绘制验证码
        StringBuffer randomCode = new StringBuffer();
        for (int i = 0; i < CAPTCHA_COUNT; i++) {
            String code = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
            int red = random.nextInt(255);
            int green = random.nextInt(255);
            int blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawString(code, (i + 1) * CAPTCHA_CODE_X, CAPTCHA_CODE_Y);
            // 拼接
            randomCode.append(code);
        }
        // 输出到客户端
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        ImageIO.write(buffImg, "jpeg", response.getOutputStream());
        // 保存到redis
        redisService.set(Constants.CAPTCHA_KEY + "_" + request.getRequestURL(), randomCode);
    }

    /**
     * 生成图像验证码
     * 限制每分钟请求十次
     *
     * @param request
     * @param response
     */
    @RequestMapping("/getCaptcha")
    @AccessLimit(seconds = 60, maxCount = 10, needLogin = false)
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        try {
            out(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证图像验证码
     * 限制每分钟请求十次
     *
     * @param request
     * @param code
     * @return
     */
    @RequestMapping("/checkCaptcha")
    @ResponseBody
    @AccessLimit(seconds = 60, maxCount = 10, needLogin = false)
    public Result checkCaptcha(HttpServletRequest request,
                               @RequestParam @Size(min = 4,max = 4) String code) {
        String redisCode = (String) redisService.get(Constants.CAPTCHA_KEY + "_" + request.getRequestURL());
        if (redisCode == null) {
            return Result.error(Status.CODE_EXPIRED);
        }
        if (redisCode.toLowerCase().equals(code.toLowerCase())) {
            return Result.error(Status.CODE_ERROR);
        } else {
            return Result.success("");
        }
    }
}
