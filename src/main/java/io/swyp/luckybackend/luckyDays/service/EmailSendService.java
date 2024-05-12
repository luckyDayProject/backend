package io.swyp.luckybackend.luckyDays.service;

import io.swyp.luckybackend.luckyDays.dto.SendMailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailSendService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(List<SendMailDto> mailDtos) throws MessagingException {
        MimeMessage mail = mailSender.createMimeMessage();

        for (SendMailDto mailDto : mailDtos) {
            try {
                MimeMessageHelper mailHelper = new MimeMessageHelper(mail, true, "UTF-8");
                mailHelper.setFrom("LuckyDay <songhr95@gmail.com>");
                mailHelper.setTo(mailDto.getTo()); // 'getTo' 메서드를 사용하여 각 dto의 수신자 이메일을 설정
                mailHelper.setSubject(mailDto.getSubject()); // 'getSubject' 메서드를 사용하여 각 dto의 제목을 설정
                mailHelper.setText(mailDto.getContent(), true); // 'getContent' 메서드를 사용하여 각 dto의 내용을 설정

                mailSender.send(mail); // 메일 전송
                // 메일 객체를 재생성하거나, 기존 메일 객체를 재사용하기 전에 초기화 필요
                mail = mailSender.createMimeMessage();
            } catch (MessagingException e) {
                e.printStackTrace(); // 실제 응용 프로그램에서는 로깅 프레임워크를 사용할 것을 권장
            }
        }
    }
}
