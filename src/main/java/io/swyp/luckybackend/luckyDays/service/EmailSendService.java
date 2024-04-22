package io.swyp.luckybackend.luckyDays.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSendService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail() throws MessagingException {
        /*SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("hrs0518@kakao.com");
        message.setSubject("LuckyDay!");

        message.setText("https://www.shutterstock.com/image-vector/slogan-lucky-day-make-today-260nw-1884537988.jpg \n내일은 당신의 Lucky Day입니다! 마음의 준비를 마치셨나요!");
        mailSender.send(message);*/

        String content = "내일은 당신의 Lucky Day입니다! 마음의 준비를 마치셨나요!" + "<img src=\"https://www.shutterstock.com/image-vector/slogan-lucky-day-make-today-260nw-1884537988.jpg\">";

        MimeMessage mail = mailSender.createMimeMessage();
        MimeMessageHelper mailHelper = new MimeMessageHelper(mail,true,"UTF-8");
        mailHelper.setFrom("LuckyDay <songhr95@gmail.com>");
        mailHelper.setTo("hrs0518@kakao.com");
        mailHelper.setSubject("LuckyDay!");
        mailHelper.setText(content, true);
        mailSender.send(mail);

    }
}
