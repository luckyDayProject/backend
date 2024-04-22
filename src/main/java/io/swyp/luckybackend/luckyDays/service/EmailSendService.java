package io.swyp.luckybackend.luckyDays.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSendService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("hrs0518@kakao.com");
        message.setTo("hrs0518@kakao.com");
        message.setSubject("LuckyDay!");
        message.setText("내일은 당신의 Lucky Day입니다! 마음의 준비를 마치셨나요!");
        mailSender.send(message);
    }
}
