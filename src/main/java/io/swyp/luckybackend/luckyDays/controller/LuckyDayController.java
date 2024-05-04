package io.swyp.luckybackend.luckyDays.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swyp.luckybackend.common.ResponseDTO;
import io.swyp.luckybackend.luckyDays.dto.CreateLcDayRequestDto;
import io.swyp.luckybackend.luckyDays.service.LuckyDayService;
import io.swyp.luckybackend.users.dto.ModifyUserRequestDto;
import jakarta.servlet.http.HttpServletRequest;
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

    @Operation(summary = "활동 목록 조회")
    @GetMapping("/activity")
    public ResponseEntity<ResponseDTO> activityList(){
        return luckyDayService.getActivityList();
    }

    @Operation(summary = "럭키데이 생성 API")
    @PostMapping("")
    public ResponseEntity<ResponseDTO> createLcDay(HttpServletRequest request, @RequestBody CreateLcDayRequestDto requestDto){
        String token = request.getHeader("Authorization");
        return luckyDayService.createLcDay(token, requestDto);
    }

    @Operation(summary = "유저 럭키데이 목록 조회")
    @GetMapping("")
    public ResponseEntity<ResponseDTO> getLcDayList(HttpServletRequest request, @RequestParam(name = "isCurrent", required = false, defaultValue = "1") int isCurrent) {
        String token = request.getHeader("Authorization");
        return luckyDayService.getLcDayList(token, isCurrent);
    }

    @Operation(summary = "유저 럭키데이 상세 조회")
    @GetMapping("/{dtlNo}")
    public ResponseEntity<ResponseDTO> getLcDayDetail(HttpServletRequest request, @PathVariable int dtlNo) {
        String token = request.getHeader("Authorization");
        return luckyDayService.getLcDayDetail(token, dtlNo);
    }

    @Operation(summary = "유저 럭키데이 싸이클 정보 조회")
    @GetMapping("/info/{cyclNo}")
    public ResponseEntity<ResponseDTO> getLcDayCyclInfo(HttpServletRequest request, @PathVariable int cyclNo) {
        String token = request.getHeader("Authorization");
        return luckyDayService.getLcDayCyclInfo(token, cyclNo);
    }

    @Operation(summary = "유저 럭키데이 리셋")
    @DeleteMapping("")
    public ResponseEntity<ResponseDTO> deleteLcDayCycl(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return luckyDayService.deleteLcDayCycl(token);
    }


}
