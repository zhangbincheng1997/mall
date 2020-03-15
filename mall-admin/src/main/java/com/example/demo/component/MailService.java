package com.example.demo.component;

import com.alibaba.fastjson.JSON;
import com.example.demo.entity.OrderMaster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MailService {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    public void send(String to, OrderMaster orderMaster) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("下单成功");
        message.setText(JSON.toJSONString(orderMaster));

        try {
            // 发送邮件
            mailSender.send(message);
            log.info(message.toString());
        } catch (Exception e) {
            log.error("邮件发送失败", e);
        }
    }
}
