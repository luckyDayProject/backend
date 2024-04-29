package io.swyp.luckybackend.users.service;

import io.swyp.luckybackend.users.dto.SignInRequestDto;
import io.swyp.luckybackend.users.dto.SignInResponseDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto requestDto);
}
