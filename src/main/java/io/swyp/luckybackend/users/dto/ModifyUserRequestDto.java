package io.swyp.luckybackend.users.dto;

import io.swyp.luckybackend.users.domain.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ModifyUserRequestDto {
    private String nickname;
    private String email;

    @Builder
    public ModifyUserRequestDto(String nickname, String email){
        this.nickname = nickname;
        this.email = email;
    }

    public UserEntity modifyDto2Entity(String nickname, String email){
        return UserEntity.builder().nickname(nickname).email(email).build();
    }
}
