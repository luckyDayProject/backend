package io.swyp.luckybackend.users.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignInRequestDto {
    private long userNo;
    private String password;

    @Override
    public String toString() {
        return "SignInRequestDto{" +
                "userNo=" + userNo +
                ", password='" + password + '\'' +
                '}';
    }
}
