package io.swyp.luckybackend.users.dto;

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

}
