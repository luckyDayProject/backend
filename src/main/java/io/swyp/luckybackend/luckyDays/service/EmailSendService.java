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
        // 메일 보낼 유저 list 가져오기(user name, dtlNo, 이미지)
        String userName = "혜린";
        String content = userName + " 님을 위한 럭키 데이가 드디어 내일이에요.\n" +
                "나에게 어떤 럭키한 하루가 기다리고 있을지, 기대되지 않나요?\n" +
                "내일 잊지 말고 아래 버튼을 클릭해서 특별한 럭키 데이 활동을 확인해 보세요.\n" +
                "[럭키 데이 확인하러 가기 \uD83C\uDF40]\n" +
                "특별한 활동을 가이드 삼아, 우연과 행복이 함께하는 럭키 데이 보내시기를 바라요!☺\uFE0F \n" + "<img src=\"https://www.shutterstock.com/image-vector/slogan-lucky-day-make-today-260nw-1884537988.jpg\">";
        MimeMessage mail = mailSender.createMimeMessage();
        MimeMessageHelper mailHelper = new MimeMessageHelper(mail,true,"UTF-8");
        mailHelper.setFrom("LuckyDay <songhr95@gmail.com>");
        mailHelper.setTo("hrs0518@kakao.com");
        mailHelper.setSubject(userName + "님, 럭키 데이가 내일이에요! \uD83D\uDC8C!");
        mailHelper.setText(content, true);
        mailSender.send(mail);

    }
}
