package io.swyp.luckybackend.users.service;

import io.swyp.luckybackend.common.ResponseDTO;
import io.swyp.luckybackend.users.domain.UserEntity;
import org.springframework.http.ResponseEntity;

public interface UserService {
    UserEntity getUserEntityByUserNo(long userNo);
    long getUserNo(String token);
    ResponseEntity<ResponseDTO> getUserInfo(String token);
    void modifyUserInfo(String token, UserEntity user);
    long deleteUser(String token);
}
