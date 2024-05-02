package io.swyp.luckybackend.users.service;

import io.swyp.luckybackend.common.JwtProvider;
import io.swyp.luckybackend.common.ResponseDTO;
import io.swyp.luckybackend.users.domain.UserEntity;
import io.swyp.luckybackend.users.dto.GetUserInfoResponseDto;
import io.swyp.luckybackend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public UserEntity getUserEntityByUserNo(long userNo){
        return userRepository.findByUserNo(userNo);
    }

    public long getUserNo(String token){
        return jwtProvider.getUserNo(token.substring(7));
    }

    public ResponseEntity<ResponseDTO> getUserInfo(String token){
        long userNo = getUserNo(token);
        UserEntity user = userRepository.findByUserNo(userNo);
        return ResponseDTO.success(GetUserInfoResponseDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname()).build());
    }

}
