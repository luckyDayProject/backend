package io.swyp.luckybackend.users.dto;

import io.swyp.luckybackend.users.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyUserRequestDto {
    private String nickname;
    private String email;

    public UserEntity modifyDto2Entity(String nickname, String email){
        return UserEntity.builder().nickname(nickname).email(email).build();
    }
}
