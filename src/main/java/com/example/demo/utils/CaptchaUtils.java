package com.example.demo.utils;

import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaUtils {

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

    /**
     * 生成验证码
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public static void out(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
        // 保存到session
        request.getSession().setAttribute(Constants.CAPTCHA_KEY, randomCode.toString());
    }

    /**
     * 检查验证码
     *
     * @param request
     * @param code
     * @return
     */
    public static boolean check(HttpServletRequest request, String code) {
        String key = (String) request.getSession().getAttribute(Constants.CAPTCHA_KEY);
        if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(key)) {
            return code.toUpperCase().equals(key);
        }
        return false;
    }
}
