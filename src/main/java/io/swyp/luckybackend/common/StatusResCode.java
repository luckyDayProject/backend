package io.swyp.luckybackend.common;

public enum StatusResCode {
    // 1000 번대 에러 : user 관련 에러


    // 2000 번대 에러 : luckyday 관련 에러


    // 3000 번대 에러 : db 에러

    ;


    private final int code;
    private final String message;

    StatusResCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
