package io.swyp.luckybackend.users.dto;

import io.swyp.luckybackend.common.ResponseDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInResponseDto {

    private String nickname;
    private int expirationTime;

    @Builder
    public SignInResponseDto(String nickname){
        super();
        this.nickname = nickname;
        this.expirationTime = 3600;
    }
    public ResponseDTO success (){
        SignInResponseDto responseBody = new SignInResponseDto(this.nickname);
        return new ResponseDTO(responseBody);
    }
//
//    public ResponseEntity<ResponseDTO> signInFail (){
//        ResponseDTO responseBody = new ResponseDTO(ResponseCode.SIGN_IN_FAIL, ResponseCode.SIGN_IN_FAIL);
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
//    }

}
