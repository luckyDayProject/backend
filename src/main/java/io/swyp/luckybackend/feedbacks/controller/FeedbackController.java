package io.swyp.luckybackend.feedbacks.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swyp.luckybackend.common.ResponseDTO;
import io.swyp.luckybackend.feedbacks.dto.FeedbackContentDto;
import io.swyp.luckybackend.feedbacks.service.FeedbackService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FeedbackController {
    private final FeedbackService feedbackService;
    /*
        프론트로부터 받아야 할 내용
        1. accessToken
        2. content

        백에서 줄 내용
        1. success 응답 or fail 응답
    */

    @Operation(summary = "피드백 전송")
    @PostMapping(value = "/feedback")
    public ResponseEntity<ResponseDTO> sendFeedback(HttpServletRequest request,
                                                    @RequestBody FeedbackContentDto feedbackContentDto) {
        String token = request.getHeader("Authorization");

        return feedbackService.sendFeedback(token, feedbackContentDto.getContent());
    }


}
