package io.swyp.luckybackend.luckyDays.service;

import io.swyp.luckybackend.common.JwtProvider;
import io.swyp.luckybackend.common.ResponseDTO;
import io.swyp.luckybackend.luckyDays.domain.LcDayDtlEntity;
import io.swyp.luckybackend.luckyDays.dto.*;
import io.swyp.luckybackend.luckyDays.repository.LcActivityRepository;
import io.swyp.luckybackend.luckyDays.repository.LcDayDtlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LuckyDayService {
    private final LcActivityRepository lcActivityRepository;
    private final LcDayDtlRepository lcDayDtlRepository;
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

        // '직접 입력' 카테고리 처리
        String directInputCategoryName = "직접 입력";
        List<GetActivityListDto> directInputActivities = groupedActivities.getOrDefault(directInputCategoryName, Collections.emptyList());

        // '직접 입력'에서 하나 랜덤 선택
        GetActivityListDto randomDirectInputActivity = null;
        if (!directInputActivities.isEmpty()) {
            Random random = new Random();
            randomDirectInputActivity = directInputActivities.get(random.nextInt(directInputActivities.size()));
        }

        // 결과를 원하는 JSON 형식으로 변환
        List<CategoryActivitiesDTO> categoryActivities = groupedActivities.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(directInputCategoryName))
                .map(entry -> new CategoryActivitiesDTO(entry.getKey(), mapActivities(entry.getValue())))
                .collect(Collectors.toList());

        // '직접 입력' 카테고리 추가
        if (randomDirectInputActivity != null) {
            List<ActivityDTO> directInputList = List.of(new ActivityDTO(randomDirectInputActivity.getActivityNo(), randomDirectInputActivity.getKeyword()));
            categoryActivities.add(new CategoryActivitiesDTO(directInputCategoryName, directInputList));
        }

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
        /*
            에러코드 처리
            1. 이미 생성된 싸이클이 있을경우 (이미 싸이클이 있으면 생성 버튼을 누를수가 없는데 굳이 처리를 해야하나?)
        */
        long userNo = getUserNo(token);

        return ResponseDTO.success("생성완료");
    }

    public ResponseEntity<ResponseDTO> getLcDayList(String token, int isCurrent) {
        /*
            에러코드 처리
            1. 생성된 싸이클이 없을 경우
        */
        long userNo = getUserNo(token);
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        List<GetLcDayListDto> lcDayList;
        if(isCurrent == 0) {
            lcDayList = lcActivityRepository.getLcDayListByHist(userNo, today);
        } else {
            lcDayList = lcActivityRepository.getLcDayList(userNo, today);
            for(GetLcDayListDto list : lcDayList) {
                if(list.getDDay() > 3) {
                    list.setDate(null);
                    list.setDDay(null);
                }
            }
        }

        return ResponseDTO.success(lcDayList);
    }

    public ResponseEntity<ResponseDTO> getLcDayDetail(String token, int dtlNo) {
        long userNo = getUserNo(token);
        GetLcDayDtlDto lcDetail = lcActivityRepository.getLcDayDetail(dtlNo);
        // 클라이언트용 이미지 URL 설정
        String imageUrl = lcDetail.getImageName() != null ? "/images/" + encodeUrl(lcDetail.getImageName()) : null;

        // 빌더를 사용하여 객체 생성
        GetLcDayDtlResDto lcDayDtlResDto = GetLcDayDtlResDto.builder()
                .dDay(lcDetail.getDDay())
                .actNm(lcDetail.getActNm())
                .actInfo(lcDetail.getActInfo())
                .review(lcDetail.getReview())
                .imageName(lcDetail.getImageName())
                .imagePath(lcDetail.getImagePath())
                .imageUrl(imageUrl)
                .build();

        return ResponseDTO.success(lcDayDtlResDto);
    }

    public ResponseEntity<ResponseDTO> getLcDayCyclInfo(String token, int cyclNo) {
        long userNo = getUserNo(token);
        GetLcDayCyclDto lcCycl = lcActivityRepository.getLcDayCyclInfo(cyclNo);
        return ResponseDTO.success(lcCycl);
    }

    @Transactional
    public ResponseEntity<ResponseDTO> deleteLcDayCycl(String token) {
        /*
            에러코드 처리
            1. 이미 리셋처리된 경우
            2. 생성된 싸이클이 없을 경우
        */
        long userNo = getUserNo(token);
        lcActivityRepository.deleteLcDayCycl(userNo);
        return ResponseDTO.success();
    }

    public ResponseEntity<ResponseDTO> insertImage(String token, int dtlNo, MultipartFile image) throws IOException {
        long userNo = getUserNo(token);

        String imagePath = "/root/lucky/luckyImage";
        File imageDirectory = new File(imagePath);

        // 디렉토리가 없으면 생성
        if (!imageDirectory.exists()) {
            imageDirectory.mkdirs();
        }

        UUID uuid = UUID.randomUUID();
        String imageName = uuid + "_" + encodeUrl(image.getOriginalFilename());
        File saveFile = new File(imagePath, imageName);

        image.transferTo(saveFile);
        imagePath = imagePath + imageName;

        String imageUrl = "/images/" + encodeUrl(imageName); // 클라이언트용 이미지 URL 설정

        lcDayDtlRepository.insertImage(dtlNo, imageName, imagePath);

        ReviewImageDto reviewImageDto = new ReviewImageDto();
        reviewImageDto.setImgaeUrl(imageUrl);

        return ResponseDTO.success(reviewImageDto);

    }

    public ResponseEntity<ResponseDTO> insertReview(String token, ReviewReqDto requestDto) throws IOException {
        Long userNo = getUserNo(token);

        lcDayDtlRepository.insertReview(requestDto.getDtlNo(), requestDto.getReview());

        return ResponseDTO.success();
    }

    private String encodeUrl(String url) {
        return URLEncoder.encode(url, StandardCharsets.UTF_8);
    }
}
