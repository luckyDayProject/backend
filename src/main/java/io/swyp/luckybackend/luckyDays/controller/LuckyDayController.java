package io.swyp.luckybackend.luckyDays.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swyp.luckybackend.common.ResponseDTO;
import io.swyp.luckybackend.luckyDays.dto.CreateLcDayRequestDto;
import io.swyp.luckybackend.luckyDays.service.LuckyDayService;
import io.swyp.luckybackend.users.dto.ModifyUserRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/luckydays")
@Slf4j
public class LuckyDayController {
    private final LuckyDayService luckyDayService;

    @Operation(summary = "활동 목록 조회 API")
    @GetMapping("/activity")
    public ResponseEntity<ResponseDTO> activityList(){
        return luckyDayService.getActivityList();
    }

    @Operation(summary = "럭키데이 생성 API")
    @PostMapping("")
    public ResponseEntity<ResponseDTO> createLcDay(@RequestHeader("Authorization") String token, @RequestBody CreateLcDayRequestDto requestDto){
        return luckyDayService.createLcDay(token, requestDto);
    }


}
