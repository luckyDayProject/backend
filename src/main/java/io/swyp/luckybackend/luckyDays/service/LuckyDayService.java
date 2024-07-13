package io.swyp.luckybackend.luckyDays.service;

import io.swyp.luckybackend.common.JwtProvider;
import io.swyp.luckybackend.common.ResponseDTO;
import io.swyp.luckybackend.common.StatusResCode;
import io.swyp.luckybackend.luckyDays.domain.*;
import io.swyp.luckybackend.luckyDays.dto.*;
import io.swyp.luckybackend.luckyDays.repository.*;
import io.swyp.luckybackend.users.domain.UserEntity;
import io.swyp.luckybackend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
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

    public long getUserNo(String token) {
        return jwtProvider.getUserNo(token.substring(7));
    }

    /**
     * 럭키데이 생성 유효성 검사
     * 1. 진행중인 럭키데이가 존재합니다.
     * 2. 기간 별 허용된 럭키데이 수를 초과합니다.
     * 3. 럭키데이 수가 선택한 활동 목록을 초과합니다.
     * 4. 럭키데이 제외 일수가 조건에 맞지 않습니다.
     * 5. 사용자 입력 내용을 작성해 주세요.
     */
    public ResponseEntity<ResponseDTO> createValidationCheck(String token, CreateLcDayRequestDto requestDto) {
        long userNo = getUserNo(token);
        boolean isExist = lcDayDtlRepository.existsByUserNoAndDDayNotPassed(userNo, LocalDate.now());
        boolean chkExceedCntPeriod = chkExceedCntPeriod(requestDto);
        boolean chkExceedCntActivity = chkExceedCntActivity(requestDto);
        boolean chkInvalidExptDays = chkInvalidExptDays(requestDto);
        boolean isMissingCustomActivity = !isMissingCustomActivity(requestDto);
        if (isExist) {
            System.out.println("isExist: " + true);
            return ResponseDTO.error(StatusResCode.EXISTED_LUCKY_CYCLE.getCode(), StatusResCode.EXISTED_LUCKY_CYCLE.getMessage());
        } else if (!chkExceedCntPeriod) {
            System.out.println("isExceedCntPeriod: " + false);
            return ResponseDTO.error(StatusResCode.EXCEEDED_CNT_PERIOD.getCode(), StatusResCode.EXCEEDED_CNT_PERIOD.getMessage());
        } else if (!chkExceedCntActivity) {
            return ResponseDTO.error(StatusResCode.EXCEEDED_CNT_ACTIVITY.getCode(), StatusResCode.EXCEEDED_CNT_ACTIVITY.getMessage());
        } else if (chkInvalidExptDays) {
            return ResponseDTO.error(StatusResCode.INVALID_EXPT_DAYS.getCode(), StatusResCode.INVALID_EXPT_DAYS.getMessage());
        } else if (isMissingCustomActivity) {
            return ResponseDTO.error(StatusResCode.MISSING_CUSTOM_ACTIVITY.getCode(), StatusResCode.MISSING_CUSTOM_ACTIVITY.getMessage());
        }

        return null;
    }

    //2. 기간 별 허용된 럭키데이 수를 초과합니다.
    private boolean chkExceedCntPeriod(CreateLcDayRequestDto requestDto) {
        int cnt = requestDto.getCnt();
        switch (requestDto.getPeriod()) {
            case 7, 14 -> {
                if (0 < cnt && cnt <= 2) return true;
            }
            case 30 -> {
                if (0 < cnt && cnt <= 4) return true;
            }
            case 60 -> {
                if (0 < cnt && cnt <= 7) return true;
            }
            default -> {
                return false;
            }
        }
        return false;
    }

    //3. 럭키데이 수가 선택한 활동 목록을 초과합니다.
    private boolean chkExceedCntActivity(CreateLcDayRequestDto requestDto) {
        return requestDto.getActList().size() >= requestDto.getCnt();
    }

    //4. 럭키데이 제외 일수가 조건에 맞지 않습니다.
    private boolean chkInvalidExptDays(CreateLcDayRequestDto requestDto) {
        LocalDate today = LocalDate.now();
        LocalDate endPeriod = today.plusDays(requestDto.getPeriod());
        List<LocalDate> excludedDates = requestDto.getExpDTList();

        for (LocalDate date : excludedDates) {
            if (date.isBefore(today) || date.isAfter(endPeriod)) {
                return true;
            }
        }
        return false;
    }

    //5. 사용자 입력 내용을 작성해 주세요.
    private boolean isMissingCustomActivity(CreateLcDayRequestDto requestDto) {
        return requestDto.getActList().stream()
                .filter(num -> num == 0)
                .count() == requestDto.getCustomActList().size();
    }


    public ResponseEntity<ResponseDTO> getActivityList() {
        List<GetActivityListDto> activities = lcActivityRepository.getActivityList();

        // 카테고리별로 그룹화
        Map<String, List<GetActivityListDto>> groupedActivities = activities.stream()
                .collect(Collectors.groupingBy(GetActivityListDto::getCategory));

        // '직접 입력' 카테고리 처리
        String directInputCategoryName = "직접 입력";

        // 결과를 원하는 JSON 형식으로 변환
        List<CategoryActivitiesDto> categoryActivities = groupedActivities.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(directInputCategoryName))
                .map(entry -> new CategoryActivitiesDto(entry.getKey(), mapActivities(entry.getValue())))
                .collect(Collectors.toList());

        // '직접 입력' 카테고리 추가
        List<ActivityDto> directInputList = List.of(new ActivityDto(0L, null));
        categoryActivities.add(new CategoryActivitiesDto(directInputCategoryName, directInputList));

        return ResponseDTO.success(categoryActivities);

    }


    private List<ActivityDto> mapActivities(List<GetActivityListDto> activities) {
        return activities.stream()
                .map(a -> new ActivityDto(a.getActivityNo(), a.getKeyword()))
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
            2. 선택한 활동의 수가 cnt보다 적은 경우
            3. 0(직접입력)을 선택하였으나 직접입력을 하지 않았을 경우
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
        LcDayCycleEntity latestCycle = lcDayCycleRepository.findTopByUserAndResetOrderByCyclNoDesc(user, "N");

        if (latestCycle != null) {
            latestCycle.changeYArchive();
        }
        LcDayCycleEntity lcDayCycle = createLcDayCycle(user, requestDto);
        lcDayCycleRepository.save(lcDayCycle);
        List<Integer> dtlOrders = createDtlOrder(cnt);
        for (int i = 0; i < cnt; i++) {
            int no = (int) (Math.random() * 2) + 1;
            LcMsgEntity msg = lcMsgRepository.findById((long) no).orElseThrow();
            LcDayDtlEntity lcDayDtl = LcDayDtl(user, requestDto, lcDayCycle, dateList.get(i), actList.get(i), dtlOrders.get(i));
            assert lcDayDtl != null;
            lcDayDtlRepository.save(lcDayDtl);
            String createContent = createContent(user.getNickname(), msg.getContent(), lcDayDtl.getDtlNo(), msg.getImg());

            String sj = user.getNickname() + msg.getSj();
            lcAlarmRepository.save(LcAlarmEntity.builder()
                    .alarmTyCode("email")
                    .user(user)
                    .dtl(lcDayDtl)
                    .sj(sj)
                    .content(createContent)
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
                .archive("N")
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
    private LcDayDtlEntity LcDayDtl(UserEntity user, CreateLcDayRequestDto requestDto, LcDayCycleEntity
            lcDayCycle, LocalDate date, ActDTO4Create actDTO4Create, Integer dtlOrder) {
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

    private String createContent(String userName, String content, long dtnNo, String imageName) {
        String style = "<br><p>님을";
        String[] contentStyle = content.split(style);
        String buttonPhrase = "럭키 데이 확인하러 가기 🍀";
        String[] parts = contentStyle[1].split(buttonPhrase);
        String imageBaseUrl = "https://223.130.131.239.nip.io/lucky/images/msg/";
        String url = "<a href=\"https://luckyday.swygbro.com/luckyboard\" style=\"background-color: #FFD700; color: black; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-weight: bold;\">";
        return contentStyle[0] +
                "<img src=\"" + imageBaseUrl + imageName + "\" style=\"width: 80%; height: auto; max-width: 600px; display: block; margin: auto;\"/>" +
                "<br>" + userName + "님을" + parts[0] + url + buttonPhrase + "</a>" + parts[1];
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

        try {
            if (isCurrent == 0) {
                if (cyclNo != null) {
                    // 이력 조회 (럭키데이 보관함)
                    lcDayList = lcActivityRepository.getLcDayListByHist(userNo, cyclNo, today);

                    if (lcDayList.isEmpty()) {
                        return ResponseDTO.error(StatusResCode.NOT_EXISTED_HIST_LDay.getCode(), StatusResCode.NOT_EXISTED_HIST_LDay.getMessage());
                    }

                } else {
                    // 현재 싸이클 이면서 지난 럭키데이 조회
                    lcDayList = lcActivityRepository.getPastLcDayList(userNo, today);

                    if (lcDayList.isEmpty()) {
                        return ResponseDTO.error(StatusResCode.NOT_EXISTED_HIST_LDay.getCode(), StatusResCode.NOT_EXISTED_HIST_LDay.getMessage());
                    }

                }
            } else {
                // 현재 싸이클에서 오늘 이후의 럭키데이 조회
                lcDayList = lcActivityRepository.getLcDayList(userNo, today);

                if (lcDayList.isEmpty()) {
                    return ResponseDTO.error(StatusResCode.NOT_EXISTED_CURRENT_CYCLE.getCode(), StatusResCode.NOT_EXISTED_CURRENT_CYCLE.getMessage());
                }

                countLcDdays(lcDayList);
            }

            return ResponseDTO.success(lcDayList);

        } catch (Error e) {
            log.error(e.getMessage());
            throw e;
        }

    }

    private void countLcDdays(List<GetLcDayListDto> lcDayList) {
        for (GetLcDayListDto list : lcDayList) {
            if (list.getDDay() > 3) {
                list.setDate(null);
                list.setDDay(null);
            }
        }
    }

    public ResponseEntity<ResponseDTO> getLcDayDetail(String token, int dtlNo) {
        long userNo = getUserNo(token);
        try {
            GetLcDayDtlDto lcDetail = lcActivityRepository.getLcDayDetail(dtlNo);
            if (lcDetail == null) {
                return ResponseDTO.error(StatusResCode.NOT_EXISTED_DTL_NO.getCode(), StatusResCode.NOT_EXISTED_DTL_NO.getMessage());
            }
            String category = lcActivityRepository.findCategoryByActivityNm(lcDetail.getActNm());

            // 클라이언트용 이미지 URL 설정
            String decodedPath = URLDecoder.decode(lcDetail.getImagePath() != null ? lcDetail.getImagePath() : "", StandardCharsets.UTF_8);
            String imageUrl = lcDetail.getImageName() != null ? "/images/" + decodedPath : null;

            // 빌더를 사용하여 객체 생성
            GetLcDayDtlResDto lcDayDtlResDto = GetLcDayDtlResDto.builder()
                    .dDay(lcDetail.getDDay())
                    .actNm(lcDetail.getActNm())
                    .actInfo(lcDetail.getActInfo())
                    .review(lcDetail.getReview())
                    .imageName(lcDetail.getImageName())
                    .imagePath(lcDetail.getImagePath())
                    .imageUrl(imageUrl)
                    .category(category)
                    .build();

            return ResponseDTO.success(lcDayDtlResDto);
        } catch (Error e) {
            log.error(e.getMessage());
            throw e;
        }

    }

    public ResponseEntity<ResponseDTO> getLcDayCyclInfo(String token, int cyclNo) {
        long userNo = getUserNo(token);
        try {
            GetLcDayCyclDto lcCycl = lcActivityRepository.getLcDayCyclInfo(cyclNo);
            if (lcCycl == null) {
                return ResponseDTO.error(StatusResCode.NOT_EXISTED_CURRENT_CYCLE.getCode(), StatusResCode.NOT_EXISTED_CURRENT_CYCLE.getMessage());
            }
            return ResponseDTO.success(lcCycl);
        } catch (Error e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Transactional
    public ResponseEntity<ResponseDTO> deleteLcDayCycl(String token) {

        Long userNo = getUserNo(token);

        try {
            // 1. 최근 cyclNo 조회
            Long latestCyclNo = lcActivityRepository.findLatestCyclNo(userNo);
            if (latestCyclNo == null) {
                return ResponseDTO.error(StatusResCode.NOT_EXISTED_CYCLE_NO.getCode(), StatusResCode.NOT_EXISTED_CYCLE_NO.getMessage());
            } else {
                // 2. delete
                int delResult = lcActivityRepository.deleteLcDayCycl(userNo, latestCyclNo);

                // 3. t_alarm status update
                if (delResult > 0) {
                    lcActivityRepository.updateAlarmStatus(userNo, latestCyclNo);
                }
                return ResponseDTO.success();
            }

        } catch (Error e) {
            log.error(e.getMessage());
            throw e;
        }
    }


    public ResponseEntity<ResponseDTO> insertReview(String token, ReviewReqDto requestDto, MultipartFile image) throws
            IOException {
        Long userNo = getUserNo(token);
        try {
            // dtlNo가 현재 user의 것인지확인
            long dtlNo = requestDto.getDtlNo();
            boolean result = lcDayDtlRepository.getUserNoByDtlNo(dtlNo, userNo);
            if (!result) {
                return ResponseDTO.error(StatusResCode.INVALID_USER.getCode(), StatusResCode.INVALID_USER.getMessage());
            }

            if (requestDto.getReview() == null) {
                return ResponseDTO.error(StatusResCode.EMPTY_CONTENT.getCode(), StatusResCode.EMPTY_CONTENT.getMessage());
            }

            if (requestDto.getReview().length() > 100) {
                return ResponseDTO.error(StatusResCode.EXCEEDED_TEXT_LENGTH.getCode(), StatusResCode.EXCEEDED_TEXT_LENGTH.getMessage());
            }

            // 첨부 이미지 처리
            if (image != null) {
                Map<String, String> settingImage = settingImages(requestDto, image, userNo);
                lcDayDtlRepository.updateReview(requestDto.getDtlNo(), requestDto.getReview(), settingImage.get("imageName"), settingImage.get("imagePath").split("/root/lucky/luckyImage/")[1], userNo);
            }
            // default 이미지 처리
            else {
                Map<String, String> settingImage = settingDefaultImages(dtlNo);
                lcDayDtlRepository.updateReview(requestDto.getDtlNo(), requestDto.getReview(), settingImage.get("imageName"), settingImage.get("imagePath").split("/root/lucky/luckyImage/")[1], userNo);
            }
            return ResponseDTO.success();
        } catch (Error e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    private Map<String, String> settingImages(ReviewReqDto requestDto, MultipartFile image, Long userNo) throws IOException {
        Map<String, String> result = new HashMap<>();
        String imagePath = "/root/lucky/luckyImage/review/";
        File imageDirectory = new File(imagePath);

        // 디렉토리가 없으면 생성
        if (!imageDirectory.exists()) {
            boolean dirCreated = imageDirectory.mkdirs();
            if (!dirCreated) {
                throw new IOException("Failed to create directory: " + imageDirectory.getAbsolutePath());
            }
        }

        String uuid = userNo + "_" + requestDto.getDtlNo();
        String imageName = uuid + "_" + image.getOriginalFilename();
        System.out.println(image.getOriginalFilename());
        imageName = imageName.replaceAll("\\s", "");
        File saveFile = new File(imagePath, imageName);

        image.transferTo(saveFile);
        imagePath = imagePath + imageName;

        String imageUrl = "/images/" + imageName; // 클라이언트용 이미지 URL 설정

        log.info("imageUrl === ", imageUrl);
        result.put("imagePath", imagePath);
        result.put("imageName", imageName);

        return result;


    }

    private Map<String, String> settingDefaultImages(Long dtlNo) throws IOException {
        Map<String, String> result = new HashMap<>();
        String imageName = null;
        String category = lcDayDtlRepository.findCategoryByDtlNo(dtlNo);

        switch (category) {
            case "특별한 선물" -> imageName = "logo_present.png";
            case "맛있는 음식" -> imageName = "logo_food.png";
            case "배움과 문화" -> imageName = "logo_culture.png";
            case "이동과 탐험" -> imageName = "logo_explore.png";
            case "일상 속 소소함" -> imageName = "logo_daily.png";
            case "직접 입력" -> imageName = "logo_daily.png";
        }

        String imagePath = "/root/lucky/luckyImage/review/default/" + imageName;
        result.put("imageName", imageName);
        result.put("imagePath", imagePath);
        return result;
    }


    public ResponseEntity<ResponseDTO> updateReview(String token, ReviewReqDto requestDto, MultipartFile image) {
        Long userNo = getUserNo(token);
        try {
            // dtlNo가 현재 user의 것인지확인
            long dtlNo = requestDto.getDtlNo();
            boolean result = lcDayDtlRepository.getUserNoByDtlNo(dtlNo, userNo);
            if (!result) {
                return ResponseDTO.error(StatusResCode.INVALID_USER.getCode(), StatusResCode.INVALID_USER.getMessage());
            }
            // 기존 리뷰 및 이미지 path select
            CheckImgAndReviewDto checkImgAndReviewDto = lcDayDtlRepository.findByDtlNo(dtlNo);
            System.out.println("checkImgAndReviewDto ==== " +  checkImgAndReviewDto.getReview());
            System.out.println("checkImgAndReviewDto ==== " +  checkImgAndReviewDto.getImageName());
            System.out.println("checkImgAndReviewDto ==== " +  checkImgAndReviewDto.getImagePath());
            // review/default/logo_culture.png
            // review/cd8d17b3-7b99-4e71-ab01-b4f9db007971_blob

            /*
            1. db에서 select한 이미지 경로가 default일 경우 (유저가 등록한 이미지가 없을 경우)
                기존대로 처리
            2. db에서 select한 이미지 경로가 review일 경우 (유저가 등록한 이미지가 이미 있을 경우)
                기존 등록했던 이미지 삭제처리 및 새로운 이미지 등록은 기존대로 처리

            */

            // 기존 이미지가 default 이미지가 아닌 경우 처리
            // todo: 1. 기존 이미지명과 수정 이미지 명이 같을 경우에는 파일 삭제하지 않고 바뀐것만 업데이트
            if(!checkImgAndReviewDto.getImagePath().contains("default")) {
                log.info("커스텀 경로 이미지");
                File oldFile = new File("/root/lucky/luckyImage/" + checkImgAndReviewDto.getImagePath());
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }

            // 새 이미지 저장
            if(image != null && !image.isEmpty()) {
                Map<String, String> settingImage = settingImages(requestDto, image, userNo);
                lcDayDtlRepository.updateReview(dtlNo, requestDto.getReview(), settingImage.get("imageName"), settingImage.get("imagePath").split("/root/lucky/luckyImage/")[1], userNo);
            } else {
                Map<String, String> settingImage = settingDefaultImages(dtlNo);
                lcDayDtlRepository.updateReview(requestDto.getDtlNo(), requestDto.getReview(), settingImage.get("imageName"), settingImage.get("imagePath").split("/root/lucky/luckyImage/")[1], userNo);
            }


            return ResponseDTO.success();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Error e) {
            log.error(e.getMessage());
            throw e;
        }

    }

    public ResponseEntity<ResponseDTO> deleteReview(String token, Long dtlNo) throws IOException {
        Long userNo = getUserNo(token);
        try {
            // dtlNo가 현재 user의 것 인지 확인
            boolean result = lcDayDtlRepository.getUserNoByDtlNo(dtlNo, userNo);
            if (!result) {
                return ResponseDTO.error(StatusResCode.INVALID_USER.getCode(), StatusResCode.INVALID_USER.getMessage());
            }

            // 기존 리뷰 및 이미지 path select
            CheckImgAndReviewDto checkImgAndReviewDto = lcDayDtlRepository.findByDtlNo(dtlNo);

            if(!checkImgAndReviewDto.getImagePath().contains("default")) {
                log.info("커스텀 경로 이미지");
                File oldFile = new File("/root/lucky/luckyImage/" + checkImgAndReviewDto.getImagePath());
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }

            lcDayDtlRepository.updateReview(dtlNo, null, null, null, userNo);

            return ResponseDTO.success();
        } catch (Error e) {
            log.error(e.getMessage());
            throw e;
        }
    }


    private String encodeUrl(String url) {
        return URLEncoder.encode(url, StandardCharsets.UTF_8);
    }


    public List<SendMailDto> getLcDay(LocalDate today) {
        try {
            LocalDate tomorrow = today.plusDays(1);
            return lcActivityRepository.getLcDay(tomorrow);
        } catch (Error e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    public ResponseEntity<ResponseDTO> getLcDayCyclList(String token) {
        long userNo = getUserNo(token);

        try {

            Long latestCyclNo = lcActivityRepository.findLatestCyclNo(userNo);
            if (latestCyclNo == null) {
                return ResponseDTO.error(StatusResCode.NOT_EXISTED_CYCLE_NO.getCode(), StatusResCode.NOT_EXISTED_CYCLE_NO.getMessage());
            }

            List<GetCyclListDto> cyclList = lcActivityRepository.getLcDayCyclList(userNo);
            if (cyclList.isEmpty()) {
                return ResponseDTO.error(StatusResCode.NOT_EXISTED_HIST_CYCLE.getCode(), StatusResCode.NOT_EXISTED_HIST_CYCLE.getMessage());
            }

            return ResponseDTO.success(cyclList);

        } catch (Error e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
