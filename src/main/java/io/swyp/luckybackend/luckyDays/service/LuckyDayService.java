package io.swyp.luckybackend.luckyDays.service;

import io.swyp.luckybackend.common.ResponseDTO;
import io.swyp.luckybackend.luckyDays.domain.LcActivityEntity;
import io.swyp.luckybackend.luckyDays.dto.CategoryActivitiesDTO;
import io.swyp.luckybackend.luckyDays.repository.LcActivityRepository;
import io.swyp.luckybackend.users.dto.GetActivityListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LuckyDayService {
    private final LcActivityRepository lcActivityRepository;
    public ResponseEntity<ResponseDTO> getActivityList() {
        List<GetActivityListDto> data = lcActivityRepository.getActivityList();
        return ResponseDTO.success(data);
    }
}
