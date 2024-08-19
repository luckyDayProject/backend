package io.swyp.luckybackend.luckyDays.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swyp.luckybackend.common.ResponseDTO;
import io.swyp.luckybackend.luckyDays.dto.CreateLcDayRequestDto;
import io.swyp.luckybackend.luckyDays.dto.ReviewReqDto;
import io.swyp.luckybackend.luckyDays.service.LuckyDayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public ResponseEntity<ResponseDTO> createLcDay(HttpServletRequest request,
                                                   @RequestBody CreateLcDayRequestDto requestDto){
        String token = request.getHeader("Authorization");
        ResponseEntity<ResponseDTO> res = luckyDayService.createValidationCheck(token, requestDto);
        if (res != null){
            return res;
        }
        return luckyDayService.createLcDay(token, requestDto);
    }

    @Operation(summary = "유저 럭키데이 목록 조회")
    @GetMapping({"/cycl", "/cycl/{cyclNo}"})
    public ResponseEntity<ResponseDTO> getLcDayList(HttpServletRequest request, @PathVariable(required = false) Long cyclNo, @RequestParam(name = "isCurrent", required = false, defaultValue = "1") int isCurrent) {
        String token = request.getHeader("Authorization");
        return luckyDayService.getLcDayList(token, cyclNo, isCurrent);
    }

    @Operation(summary = "유저 럭키데이 상세 조회")
    @GetMapping("/{dtlNo}")
    public ResponseEntity<ResponseDTO> getLcDayDetail(HttpServletRequest request, @PathVariable int dtlNo) {
        log.info("유저 럭키데이 상세 조회");
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


    @Operation(summary = "럭키데이 회고록 작성")
    @PostMapping(value = "/review", consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO> insertReview(HttpServletRequest request,
                                                    @Parameter(description = "Review data", required = true)
                                                    @RequestPart(name = "reviewReqDto") ReviewReqDto requestDto,
                                                    @RequestPart(required = false, name = "image") MultipartFile image) throws IOException {
        String token = request.getHeader("Authorization");
        return luckyDayService.insertReview(token, requestDto, image);
    }

    @Operation(summary = "럭키데이 회고록 수정")
    @PutMapping(value = "/review", consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseDTO> updateReview(HttpServletRequest request,
                                                    @Parameter(description = "Review data", required = true)
                                                    @RequestPart(name = "reviewReqDto") ReviewReqDto requestDto,
                                                    @RequestPart(required = false, name = "image") MultipartFile image) throws IOException {
        String token = request.getHeader("Authorization");
        return luckyDayService.updateReview(token, requestDto, image);
    }

    @Operation(summary = "럭키데이 회고록 삭제")
    @DeleteMapping(value = "/review")
    public ResponseEntity<ResponseDTO> deleteReview(HttpServletRequest request,
                                                    @RequestParam(name="dtlNo") Long dtlNo) throws IOException {
        String token = request.getHeader("Authorization");
        return luckyDayService.deleteReview(token, dtlNo);
    }

    @Operation(summary = "유저 럭키데이 싸이클 리스트")
    @GetMapping(value = "/cycl/list")
    public ResponseEntity<ResponseDTO> getLcDayCyclList(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return luckyDayService.getLcDayCyclList(token);
    }

}
