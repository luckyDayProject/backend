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

    public ResponseDTO(String databaseError, String databaseError1) {
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
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> success() {
        ResponseDTO responseBody = new ResponseDTO();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> error(String code, String message) {
        ResponseDTO responseBody = new ResponseDTO(String.valueOf(code), message);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }

    public static ResponseEntity<ResponseDTO> invalidToken() {
        ResponseDTO responseBody = new ResponseDTO(StatusResCode.INVALID_TOKEN.getCode(),StatusResCode.INVALID_TOKEN.getMessage());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseBody);
    }
}
