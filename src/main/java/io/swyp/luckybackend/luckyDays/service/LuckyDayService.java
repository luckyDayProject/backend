package io.swyp.luckybackend.luckyDays.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonObject;
import io.swyp.luckybackend.common.JwtProvider;
import io.swyp.luckybackend.common.ResponseDTO;
import io.swyp.luckybackend.luckyDays.domain.LcActivityEntity;
import io.swyp.luckybackend.luckyDays.dto.ActivityDTO;
import io.swyp.luckybackend.luckyDays.dto.CategoryActivitiesDTO;
import io.swyp.luckybackend.luckyDays.dto.CreateLcDayRequestDto;
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
    private final JwtProvider jwtProvider;

    private final MappingJackson2HttpMessageConverter converter;

    public long getUserNo(String token) {
        return jwtProvider.getUserNo(token.substring(7));
    }

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


    /* 1. 날짜 랜덤 선택
     *      - 제외 날짜는 기간 풀에서 제외
     *      - 첫 럭키데이는 생성일 기준 4일 후 부터 배정
     *      - 3개 이상의 럭키데이가 연달아 배정되면 안됨
     *
     * 2. 활동 선택
     *      - cnt만큼, 입력한 활동(활동 목록 + 사용자입력 목록) 중에서 랜덤 선택
     *
     * 3. 럭키데이 싸이클 생성
     *      - 회원번호, 럭키데이 갯수, 럭키데이 일수, 시작날짜(오늘), 끝 날짜(오늘 + 럭키데이 일수),
     *        럭키데이 제외 날짜(리스트), 초기화 여부("N")
     *
     * 4. 럭키데이 디테일 생성
     *      - 방금 만든 럭키데이 싸이클 NO FK로 받음
     *        유저번호(토큰에서 추출), 활동 번호(2. 활동 선택에서 나온 활동), 활동명(직접 입력때문에 활동목록에 있더라도 입력)
     *        회고록, 이미지명, 럭키데이 날짜(1. 날짜 랜덤 선택에서 나온 날짜), 럭키데이 순서(화면에 뿌려질 랜덤값)
     * */
    public ResponseEntity<ResponseDTO> createLcDay(String token, CreateLcDayRequestDto requestDto){
        long userNo = getUserNo(token);

        return ResponseDTO.success("생성완료");
    }
}
