package io.swyp.luckybackend.luckyDays.controller;

import io.swyp.luckybackend.common.ResponseDTO;
import io.swyp.luckybackend.luckyDays.service.LuckyDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/luckydays")
public class LuckyDayController {
    private final LuckyDayService luckyDayService;
    @GetMapping("/activity")
    public ResponseEntity<ResponseDTO> activityList(){
        return luckyDayService.getActivityList();
    }



}
