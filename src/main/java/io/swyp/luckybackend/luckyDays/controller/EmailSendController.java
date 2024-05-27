package io.swyp.luckybackend.luckyDays.controller;

import io.swyp.luckybackend.luckyDays.service.EmailSendService;
import io.swyp.luckybackend.scheduler.MailScheduler;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailSendController {
    @Autowired
    EmailSendService emailSendService;

    @Autowired
    MailScheduler mailScheduler;

    @GetMapping("/luckydays/sendEmail")
    public String sendEmail() throws MessagingException {
        mailScheduler.scheduleSignupTask();
        return "Email sent successfully!";
    }
}
