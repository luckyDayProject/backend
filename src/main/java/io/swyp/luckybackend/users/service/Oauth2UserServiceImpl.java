package io.swyp.luckybackend.users.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swyp.luckybackend.users.domain.CustomOAuth2User;
import io.swyp.luckybackend.users.domain.UserEntity;
import io.swyp.luckybackend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Oauth2UserServiceImpl extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
//        String oauthClientName = userRequest.getClientRegistration().getClientName();
        String oauthClientName = "kakao";

        try{
            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        }catch (Exception exception){
            exception.printStackTrace();
        }

        UserEntity userEntity = null;
        long userNo = 0l;
        String email = "a@b.com";

        if (oauthClientName.equals("kakao")){
            userNo = (long) oAuth2User.getAttributes().get("id");
            userEntity = UserEntity.builder()
                    .ageGroup(3)
                    .birthYear(1992)
                    .email(email)
                    .gender('M')
                    .nickname("홍정명")
                    .profileIconNo(1)
                    .build();
        }

        if (oauthClientName.equals("naver")){
            Map<String, String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get("response");
            userNo = Long.parseLong(responseMap.get("id").substring(0, 14));
            email = responseMap.get("email");
            userEntity = UserEntity.builder()
                    .ageGroup(3)
                    .birthYear(1992)
                    .email(email)
                    .gender('M')
                    .nickname("홍정명")
                    .profileIconNo(1)
                    .build();
        }

        userRepository.save(userEntity);


        return new CustomOAuth2User(userNo);
    }
}
