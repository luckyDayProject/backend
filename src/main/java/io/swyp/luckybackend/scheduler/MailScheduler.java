package io.swyp.luckybackend.scheduler;

import io.swyp.luckybackend.luckyDays.dto.SendMailDto;
import io.swyp.luckybackend.luckyDays.service.EmailSendService;
import io.swyp.luckybackend.luckyDays.service.LuckyDayService;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@Configuration
@Slf4j
@EnableScheduling
public class MailScheduler {
    @Autowired
    EmailSendService emailSendService;

    @Autowired
    LuckyDayService luckyDayService;

    @Value("${spring.config.activate.on-profile}")
    String profile;

    @Scheduled(cron = "0 0 6 * * *") // 매일 새벽 6시
//    @Scheduled(cron = "0 0/1 * * * *") // 매 분마다 실행
    public void scheduleSignupTask() throws MessagingException {
        if (Objects.equals(profile, "prod")) {
            // 1. 해당 날짜가 럭키데이인 list 가져오기
            LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
            List<SendMailDto> mailDto = luckyDayService.getLcDay(today);
            if (mailDto.isEmpty()) {
                log.info("No emails to send today.");
                return;
            }
            // 2. for문으로 메일 뿌리기
            emailSendService.sendEmail(mailDto);
            log.info("email scheduled");
        } else {
            log.warn("The application does not send alarm emails because it is not running in the production environment.");
        }
    }
}
