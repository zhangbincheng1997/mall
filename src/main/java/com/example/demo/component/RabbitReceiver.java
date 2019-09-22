package com.example.demo.component;

import com.example.demo.base.AppLog;
import com.example.demo.utils.Constants;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Order(2)
public class RabbitReceiver {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private RedisService redisService;

    @Value("${mail.fromMail.addr}")
    private String from;

    @RabbitHandler
    @RabbitListener(queues = Constants.TEST_TOPIC)
    public void testProcess(String message) {
        AppLog.info("Rabbit Receiver [TEST] : " + message);
    }

    @RabbitHandler
    @RabbitListener(queues = Constants.EMAIL_TOPIC)
    public void emailProcess(String to) {
        // 产生1000-9999的随机数
        int number = new Random().nextInt(9999 - 1000 + 1) + 1000;
        String code = String.valueOf(number);

        // 构造消息体
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("邮箱验证码");
        message.setText(code);

        try {
            // 发送邮件
            mailSender.send(message);
            redisService.set(Constants.EMAIL_KEY + "_" + to, code, Constants.EMAIL_EXPIRY);
            AppLog.info(message);
        } catch (Exception e) {
            AppLog.error(e);
        }
    }

}
