package io.swyp.luckybackend.luckyDays.service;

import io.swyp.luckybackend.common.JwtProvider;
import io.swyp.luckybackend.common.ResponseDTO;
import io.swyp.luckybackend.luckyDays.domain.*;
import io.swyp.luckybackend.luckyDays.dto.*;
import io.swyp.luckybackend.luckyDays.repository.*;
import io.swyp.luckybackend.users.domain.UserEntity;
import io.swyp.luckybackend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.parameters.P;
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
    private static final Logger log = LoggerFactory.getLogger(LuckyDayService.class);
    private final LcActivityRepository lcActivityRepository;
    private final LcDayDtlRepository lcDayDtlRepository;
    private final LcDayCycleRepository lcDayCycleRepository;
    private final LCAlarmRepository lcAlarmRepository;
    private final LcMsgRepository lcMsgRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    private final MappingJackson2HttpMessageConverter converter;

    public long getUserNo(String token) {
        return jwtProvider.getUserNo(token.substring(7));
    }

    public ResponseEntity<ResponseDTO> getActivityList() {
        List<GetActivityListDto> activities = lcActivityRepository.getActivityList();

        // 카테고리별로 그룹화
        Map<String, List<GetActivityListDto>> groupedActivities = activities.stream()
                .collect(Collectors.groupingBy(GetActivityListDto::getCategory));

        // '직접 입력' 카테고리 처리
        String directInputCategoryName = "직접 입력";

        // 결과를 원하는 JSON 형식으로 변환
        List<CategoryActivitiesDTO> categoryActivities = groupedActivities.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(directInputCategoryName))
                .map(entry -> new CategoryActivitiesDTO(entry.getKey(), mapActivities(entry.getValue())))
                .collect(Collectors.toList());

        // '직접 입력' 카테고리 추가
        List<ActivityDTO> directInputList = List.of(new ActivityDTO(0l, null));
        categoryActivities.add(new CategoryActivitiesDTO(directInputCategoryName, directInputList));

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
    @Transactional
    public ResponseEntity<ResponseDTO> createLcDay(String token, CreateLcDayRequestDto requestDto) {
        /*
            에러코드 처리
            1. 이미 생성된 싸이클이 있을경우 (이미 싸이클이 있으면 생성 버튼을 누를수가 없는데 굳이 처리를 해야하나?)
        */
        System.out.println("럭키데이 생성중");
        long userNo = getUserNo(token);
        int cnt = requestDto.getCnt();
        List<LocalDate> dateList = pickDate(requestDto);
        for (LocalDate date : dateList) {
            System.out.println(date);
        }
        List<ActDTO4Create> actList = pickAct(requestDto);
        UserEntity user = userRepository.findByUserNo(userNo);
        LcDayCycleEntity lcDayCycle = createLcDayCycle(user, requestDto);
        lcDayCycleRepository.save(lcDayCycle);
        List<Integer> dtlOrders = createDtlOrder(cnt);
        LcMsgEntity msg = lcMsgRepository.findById(1L).orElseThrow();
        for (int i = 0; i < cnt; i++) {
            LcDayDtlEntity lcDayDtl = LcDayDtl(user, requestDto, lcDayCycle, dateList.get(i), actList.get(i), dtlOrders.get(i));
            assert lcDayDtl != null;
            lcDayDtlRepository.save(lcDayDtl);
            String content = user.getNickname() + msg.getContent();
            String sj = user.getNickname() + msg.getSj();
            lcAlarmRepository.save(LcAlarmEntity.builder()
                    .alarmTyCode("email")
                    .user(user)
                    .dtl(lcDayDtl)
                    .sj(sj)
                    .content(content)
                    .sendYn('N')
                    .dDay(lcDayDtl.getDDay()).build());
        }

        return ResponseDTO.success("생성완료");
    }

    /* 1. 날짜 랜덤 선택
     *      - 제외 날짜는 기간 풀에서 제외
     *      - 첫 럭키데이는 생성일 기준 4일 후 부터 배정
     *      - 3개 이상의 럭키데이가 연달아 배정되면 안됨
     */
    private List<LocalDate> pickDate(CreateLcDayRequestDto requestDto) {
        LocalDate startDate = LocalDate.now().plusDays(4);
        LocalDate endDate = LocalDate.now().plusDays(requestDto.getPeriod());
        HashSet<LocalDate> dateSet = new HashSet<>(requestDto.getExpDTList());

        List<LocalDate> originalAvailableDates = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (!dateSet.contains(date)) {
                originalAvailableDates.add(date);
            }
        }

        List<LocalDate> randomDates = new ArrayList<>();
        List<LocalDate> availableDates = new ArrayList<>(originalAvailableDates); // 복사본 사용
        Random random = new Random();
        boolean valid;
        do {
            randomDates.clear(); // 이전에 선택된 날짜들을 지웁니다.
            availableDates = new ArrayList<>(originalAvailableDates); // 제거된 날짜들을 다시 복원합니다.
            while (randomDates.size() < requestDto.getCnt() && !availableDates.isEmpty()) {
                int index = random.nextInt(availableDates.size());
                randomDates.add(availableDates.remove(index));
            }
            valid = !hasThreeConsecutiveDays(randomDates); // 연속된 3일을 검사합니다.
        } while (!valid); // 유효하지 않으면 다시 시도합니다.

        return randomDates;
    }

    //    3일 연속 배정 체크
    private boolean hasThreeConsecutiveDays(List<LocalDate> dates) {
        List<LocalDate> sortedDates = new ArrayList<>(dates);
        Collections.sort(sortedDates);
        for (int i = 2; i < sortedDates.size(); i++) {
            if (sortedDates.get(i).minusDays(1).equals(sortedDates.get(i - 1)) &&
                    sortedDates.get(i - 1).minusDays(1).equals(sortedDates.get(i - 2))) {
                return true; // 연속된 3일을 찾으면 true 반환
            }
        }
        return false; // 연속된 3일이 없으면 false 반환
    }

    /* 2. 활동 선택
     *      - cnt만큼, 입력한 활동(활동 목록 + 사용자입력 목록) 중에서 랜덤 선택
     */
    private List<ActDTO4Create> pickAct(CreateLcDayRequestDto requestDto) {
        List<Integer> actList = new ArrayList<>(requestDto.getActList());
        Collections.sort(actList);
        List<Integer> actNoList = new ArrayList<>(actList);

        List<String> combinedActNameList = new ArrayList<>();
        List<String> actNameList = extractActivityNames(actNoList);
        combinedActNameList.addAll(requestDto.getCustomActList());
        combinedActNameList.addAll(actNameList);

        List<ActDTO4Create> selectedActivities = new ArrayList<>();
        Random random = new Random();
        int count = requestDto.getCnt();

        // combinedList에서 중복 없이 랜덤하게 count 만큼 선택
        while (selectedActivities.size() < count && !combinedActNameList.isEmpty()) {
            int index = random.nextInt(combinedActNameList.size());
            ActDTO4Create act = ActDTO4Create.builder()
                    .actNo(Long.valueOf(actNoList.remove(index)))
                    .activityName(combinedActNameList.remove(index))
                    .build();
            selectedActivities.add(act);
        }

        return selectedActivities;
    }

    private List<String> extractActivityNames(List<Integer> activityNos) {
        List<String> actNameList = new ArrayList<>();  // 활동 이름을 저장할 리스트

        // 각 ID에 대해 이름을 조회하고 리스트에 추가
        for (Integer activityNo : activityNos) {
            String activityName = lcActivityRepository.findActivityNameByActivityNo(Long.valueOf(activityNo));
            if (activityName != null) {  // null 체크를 통해 유효한 이름만 추가
                actNameList.add(activityName);
            }
        }
        return actNameList;
    }

    /* 3. 럭키데이 싸이클 생성
     *      - 회원번호, 럭키데이 갯수, 럭키데이 일수, 시작날짜(오늘), 끝 날짜(오늘 + 럭키데이 일수),
     *        럭키데이 제외 날짜(리스트), 초기화 여부("N")
     */
    private LcDayCycleEntity createLcDayCycle(UserEntity user, CreateLcDayRequestDto requestDto) {
        String exptDt = requestDto.getExpDTList().toString();
        return LcDayCycleEntity.builder()
                .user(user)
                .count(requestDto.getCnt())
                .period(requestDto.getPeriod())
                .startDt(java.sql.Date.valueOf(LocalDate.now()))
                .endDt(java.sql.Date.valueOf(LocalDate.now().plusDays(requestDto.getPeriod() - 1)))
                .exptDt(exptDt.substring(1, exptDt.length() - 1))
                .reset("N")
                .build();
    }

    private List<Integer> createDtlOrder(int cnt) {
        List<Integer> numbers = new ArrayList<>();

        for (int i = 0; i < cnt; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);
        return numbers;
    }

    /* 4. 럭키데이 디테일 생성
     *      - 방금 만든 럭키데이 싸이클 NO FK로 받음
     *        유저번호(토큰에서 추출), 활동 번호(2. 활동 선택에서 나온 활동), 활동명(직접 입력때문에 활동목록에 있더라도 입력)
     *        회고록, 이미지명, 럭키데이 날짜(1. 날짜 랜덤 선택에서 나온 날짜), 럭키데이 순서(화면에 뿌려질 랜덤값)
     * */
    private LcDayDtlEntity LcDayDtl(UserEntity user, CreateLcDayRequestDto requestDto, LcDayCycleEntity lcDayCycle, LocalDate date, ActDTO4Create actDTO4Create, Integer dtlOrder) {
        LcActivityEntity lcActivityEntity;

        if (actDTO4Create.getActNo() == 0) {
            Random random = new Random();
            int randomNumber = 53 + random.nextInt(5);
            lcActivityEntity = lcActivityRepository.findById((long) randomNumber).orElseThrow();
        } else {
            lcActivityEntity = lcActivityRepository.findById(actDTO4Create.getActNo()).orElseThrow();
        }
        return LcDayDtlEntity.builder()
                .cycl(lcDayCycle)
                .user(user)
                .activity(lcActivityEntity)
                .activityNm(actDTO4Create.getActivityName())
                .dDay(date)
                .dtlOrder(dtlOrder)
                .build();
    }

    public ResponseEntity<ResponseDTO> getLcDayList(String token, Long cyclNo, int isCurrent) {
    /*
        에러코드 처리 필요:
        1. 생성된 싸이클이 없을 경우 에러 처리
        2. 현재 싸이클에 아직 지난 럭키데이가 없을 경우 에러 처리
    */
        long userNo = getUserNo(token);
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        List<GetLcDayListDto> lcDayList;

        if (isCurrent == 0) {
            if (cyclNo != null) {
                // 이력 조회 (럭키데이 보관함)
                lcDayList = lcActivityRepository.getLcDayListByHist(userNo, cyclNo, today);
            } else {
                // 현재 싸이클 이면서 지난 럭키데이 조회
                lcDayList = lcActivityRepository.getPastLcDayList(userNo, today);
            }
        } else {
            // 현재 싸이클에서 오늘 이후의 럭키데이 조회
            lcDayList = lcActivityRepository.getLcDayList(userNo, today);
            clearFutureLcDays(lcDayList);
        }

        return ResponseDTO.success(lcDayList);
    }

    private void clearFutureLcDays(List<GetLcDayListDto> lcDayList) {
        for (GetLcDayListDto list : lcDayList) {
            if (list.getDDay() > 3) {
                list.setDate(null);
                list.setDDay(null);
            }
        }
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

    /*public ResponseEntity<ResponseDTO> insertImage(String token, int dtlNo, MultipartFile image) throws IOException {
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


        return ResponseDTO.success(reviewImageDto);

    }*/

    public ResponseEntity<ResponseDTO> insertReview(String token, ReviewReqDto requestDto, MultipartFile image) throws IOException {
        Long userNo = getUserNo(token);

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

        log.info("imageUrl === ", imageUrl);


        lcDayDtlRepository.insertReview(requestDto.getDtlNo(), requestDto.getReview(), imageName, imagePath);

        return ResponseDTO.success();
    }

    private String encodeUrl(String url) {
        return URLEncoder.encode(url, StandardCharsets.UTF_8);
    }

    public List<SendMailDto> getLcDay(LocalDate today) {
        return lcActivityRepository.getLcDay(today);
    }

    public ResponseEntity<ResponseDTO> getLcDayCyclList(String token) {
        long userNo = getUserNo(token);
        List<GetCyclListDto> cyclList = lcActivityRepository.getLcDayCyclList(userNo);
        return ResponseDTO.success(cyclList);
    }
}
