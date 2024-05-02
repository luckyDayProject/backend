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
import java.util.Map;
import java.util.Optional;

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
//            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        }catch (Exception exception){
            exception.printStackTrace();
        }

        UserEntity userEntity = null;
        long userNo = 0l;
        String email = "";

        if (oauthClientName.equals("kakao")){
            userNo = (long) oAuth2User.getAttributes().get("id");
            boolean isExist = userRepository.existsById(userNo);
            if (isExist) return new CustomOAuth2User(userNo);
            Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
            email = (String) kakaoAccount.get("email");
            String ageRange = (String) kakaoAccount.get("age_range");
            int ageGroup = Optional.ofNullable(ageRange)
                    .map(a -> Integer.parseInt(a.substring(0, 1)))
                    .orElse(-1); // 미동의로 값이 없으면 -1 적재
            String genderStr = (String) kakaoAccount.get("gender");
            char gender = Optional.ofNullable(genderStr)
                    .map(g -> g.equals("male") ? 'M' : 'F')
                    .orElse('U'); // 'U'는 Unknown의 의미
//            System.out.println((Map<String, Object>) kakaoAccount.get("profile").get(""));
            int profileIconNo = (int) (Math.random() * 168) + 1;
            String birthYearStr = (String) kakaoAccount.get("birthyear");
            int birthYear = Optional.ofNullable(birthYearStr)
                    .map(Integer::parseInt)
                    .orElse(-1); // 미동의 -1 적재
            Map<String, String> profile = (Map<String, String>) kakaoAccount.get("profile");
            String nickname = profile.get("nickname");
            userEntity = UserEntity.builder()
                    .userNo(userNo)
                    .ageGroup(ageGroup)
                    .birthYear(birthYear)
                    .email(email)
                    .gender(gender)
                    .nickname(nickname)
                    .profileIconNo(profileIconNo)
                    .build();
        }
//
//        if (oauthClientName.equals("naver")){
//            Map<String, String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get("response");
//            userNo = Long.parseLong(responseMap.get("id").substring(0, 14));
//            email = responseMap.get("email");
//            userEntity = UserEntity.builder()
//                    .ageGroup(3)
//                    .birthYear(1992)
//                    .email(email)
//                    .gender('M')
//                    .nickname("홍정명")
//                    .profileIconNo(1)
//                    .build();
//        }

        userRepository.save(userEntity);


        return new CustomOAuth2User(userNo);
    }
}
