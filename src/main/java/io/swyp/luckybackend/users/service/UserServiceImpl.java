package io.swyp.luckybackend.users.service;

import io.swyp.luckybackend.common.JwtProvider;
import io.swyp.luckybackend.common.ResponseDTO;
import io.swyp.luckybackend.users.domain.UserEntity;
import io.swyp.luckybackend.users.dto.GetUserInfoResponseDto;
import io.swyp.luckybackend.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public UserEntity getUserEntityByUserNo(long userNo) {
        return userRepository.findByUserNo(userNo);
    }

    @Override
    public long getUserNo(String token) {
        return jwtProvider.getUserNo(token.substring(7));
    }

    @Override
    public ResponseEntity<ResponseDTO> getUserInfo(String token) {
        long userNo = getUserNo(token);
        UserEntity user = userRepository.findByUserNo(userNo);
        return ResponseDTO.success(GetUserInfoResponseDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname()).build());
    }

    @Override
    @Transactional
    public void modifyUserInfo(String token, UserEntity modifyUser) {
        long userNo = getUserNo(token);
        UserEntity user = userRepository.findByUserNo(userNo);
        if (modifyUser.getNickname() != null && !modifyUser.getNickname().trim().isEmpty()) {
            user.changeNickname(modifyUser.getNickname());
        }
        if (modifyUser.getEmail() != null && !modifyUser.getEmail().trim().isEmpty()) {
            user.changeEmail(modifyUser.getEmail());
        }
    }

    @Override
    @Transactional
    public long deleteUser(String token) {
        long userNo = getUserNo(token);
        userRepository.deleteById(userNo);
        return userNo;
    }
}
