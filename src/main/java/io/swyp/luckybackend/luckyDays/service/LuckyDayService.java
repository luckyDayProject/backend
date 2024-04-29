package io.swyp.luckybackend.luckyDays.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonObject;
import io.swyp.luckybackend.common.ResponseDTO;
import io.swyp.luckybackend.luckyDays.domain.LcActivityEntity;
import io.swyp.luckybackend.luckyDays.dto.ActivityDTO;
import io.swyp.luckybackend.luckyDays.dto.CategoryActivitiesDTO;
import io.swyp.luckybackend.luckyDays.repository.LcActivityRepository;
import io.swyp.luckybackend.users.dto.GetActivityListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LuckyDayService {
    private final LcActivityRepository lcActivityRepository;
    private final MappingJackson2HttpMessageConverter converter;
    public ResponseEntity<ResponseDTO> getActivityList() {
        List<GetActivityListDto> activities  = lcActivityRepository.getActivityList();

        // 카테고리별로 그룹화
        Map<String, List<GetActivityListDto>> groupedActivities = activities.stream()
                .collect(Collectors.groupingBy(GetActivityListDto::getCategory));

        // 결과를 원하는 JSON 형식으로 변환
        List<CategoryActivitiesDTO> categoryActivities = groupedActivities.entrySet().stream()
                .map(entry -> new CategoryActivitiesDTO(entry.getKey(), mapActivities(entry.getValue())))
                .collect(Collectors.toList());
        return ResponseDTO.success(categoryActivities);

    }


    private List<ActivityDTO> mapActivities(List<GetActivityListDto> activities) {
        return activities.stream()
                .map(a -> new ActivityDTO(a.getActivityNo(), a.getKeyword()))
                .collect(Collectors.toList());
    }
}
