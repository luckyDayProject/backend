package io.swyp.luckybackend.common;

public enum StatusResCode {
    SUCCESS("0", "SUCCESS"),

    // 유저 서비스 관련 에러 (1000번대)
    INVALID_TOKEN("1001", "유효하지 않은 토큰입니다."),
    EXCEEDED_NICKNAME_LENGTH("1002", "최대 글자수를 초과하였습니다."),
    NOT_EXISTED_USER("1003", "존재하지 않는 계정입니다."),
    INVALID_USER("1004", "권한이 없는 유저입니다."),

    // 럭키데이 관련 에러 (2000번대)
    EXISTED_LUCKY_CYCLE("2001", "진행중인 럭키데이가 존재합니다."),
    EXCEEDED_CNT_PERIOD("2002", "기간 별 허용된 럭키데이 수를 초과합니다."),
    EXCEEDED_CNT_ACTIVITY("2003", "럭키데이 수가 선택한 활동 목록을 초과합니다."),
    INVALID_EXPT_DAYS("2004", "럭키데이 제외 일수가 조건에 맞지 않습니다."),
    MISSING_CUSTOM_ACTIVITY("2005", "사용자 입력 내용을 작성해 주세요."),
    INVALID_CYCLE_NO("2006", "존재하지 않는 싸이클 번호입니다."),

    NOT_EXISTED_CURRENT_CYCLE("2007", "진행중인 럭키데이 싸이클이 없습니다."),
    NOT_EXISTED_HIST_CYCLE("2008", "지난 럭키데이 싸이클이 없습니다."),
    NOT_EXISTED_HIST_LDay("2009", "지난 럭키데이가 없습니다."),
    NOT_EXISTED_DTL_NO("2010", "존재하지 않는 럭키데이 번호입니다."),
    EXCEEDED_IMG_SIZE("2011", "이미지 용량이 너무 큽니다."),
    EXCEEDED_TEXT_LENGTH("2012", "최대 글자수를 초과하였습니다."),
    EMPTY_CONTENT("2013", "내용을 입력해 주세요."),
    NOT_EXISTED_CYCLE_NO("2014", "생성된 싸이클 번호가 없습니다."),

    ;

    private final String code;
    private final String message;

    StatusResCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
