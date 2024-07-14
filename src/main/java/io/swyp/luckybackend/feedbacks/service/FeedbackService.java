package io.swyp.luckybackend.feedbacks.service;

import io.swyp.luckybackend.common.JwtProvider;
import io.swyp.luckybackend.common.ResponseDTO;
import io.swyp.luckybackend.feedbacks.domain.FeedBackEntity;
import io.swyp.luckybackend.feedbacks.repository.FeedbackRepository;
import io.swyp.luckybackend.luckyDays.dto.SendMailDto;
import io.swyp.luckybackend.luckyDays.service.EmailSendService;
import io.swyp.luckybackend.luckyDays.service.LuckyDayService;
import io.swyp.luckybackend.users.domain.UserEntity;
import io.swyp.luckybackend.users.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final JwtProvider jwtProvider;
    private final FeedbackRepository feedbackRepository;
    private final EmailSendService emailSendService;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(FeedbackService.class);

    public long getUserNo(String token) {
        return jwtProvider.getUserNo(token.substring(7));
    }

    public ResponseEntity<ResponseDTO> sendFeedback(String token, String feedback) {
        try {
            Long userNo = getUserNo(token);
            UserEntity user = userRepository.findByUserNo(userNo);

            // 1. 이메일 전송
            SendMailDto mailDto = SendMailDto.builder()
                    .to("enjoyluckday@gmail.com")
                    .content(feedback)
                    .subject("FeedBack")
                    .build();

            List<SendMailDto> list = new ArrayList<>();
            list.add(mailDto);

            emailSendService.sendEmail(list);

            FeedBackEntity feedbackEntity = FeedBackEntity.builder()
                    .user(user)
                    .content(feedback)
                    .build();

            // 2. db 저장
            feedbackRepository.save(feedbackEntity);
            return ResponseDTO.success("Successfully sent feedback");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (Error e) {
            log.error(e.getMessage());
            throw e;
        }

    }
}
