package io.swyp.luckybackend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Getter
@AllArgsConstructor
public class ResponseDTO {
    private String code;
    private String message;
    private Object resData;

    public ResponseDTO(){
        this.code = ResponseCode.SUCCESS;
        this.message = ResponseMessage.SUCCESS;
    }

    public ResponseDTO(Object resData){
        this.code = ResponseCode.SUCCESS;
        this.message = ResponseMessage.SUCCESS;
        this.resData = resData;
    }


    public ResponseDTO(String code, String message) {
        this.code = code;
        this.message = message;
        this.resData = null;  // resData는 null로 설정
    }

    public static ResponseEntity<ResponseDTO> databaseError(){
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> validationFail(){
        ResponseDTO responseBody = new ResponseDTO(ResponseCode.VALIDATION_FAIL, ResponseMessage.VALIDATION_FAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> success(Object resData) {
        ResponseDTO responseBody = new ResponseDTO(resData);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> success() {
        ResponseDTO responseBody = new ResponseDTO();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> error(String code, String message) {
        ResponseDTO responseBody = new ResponseDTO(code, message);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> exceedNicknameLength() {
        ResponseDTO responseBody = new ResponseDTO(StatusResCode.EXCEEDED_NICKNAME_LENGTH.getCode(),StatusResCode.EXCEEDED_NICKNAME_LENGTH.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> notExistedUser() {
        ResponseDTO responseBody = new ResponseDTO(StatusResCode.NOT_EXISTED_USER.getCode(),StatusResCode.NOT_EXISTED_USER.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
