package io.swyp.luckybackend.users.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@EqualsAndHashCode(callSuper = true)
public class LuckyOAuth2User extends DefaultOAuth2User {
    private final String nickname;
    private final String email;
    private final String birthyear;
//    private final String talk_message;
    public LuckyOAuth2User(Collection<? extends GrantedAuthority> authorities,
                      Map<String, Object> attributes, String nameAttributeKey) {
        super(Collections.singletonList(new SimpleGrantedAuthority("USER_ROLE")) , attributes, nameAttributeKey);
        this.nickname = (String) attributes.get("nickname");
        this.email = (String) attributes.get("email");
        this.birthyear = (String) attributes.get("birthyear");
//        this.talk_message = (String) attributes.get("talk_message");
    }
}