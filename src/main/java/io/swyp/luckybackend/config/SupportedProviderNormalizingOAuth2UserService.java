//package io.swyp.luckybackend.users.config;
//
//import io.swyp.luckybackend.users.domain.LuckyOAuth2User;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.util.EnumSet;
//
//@Slf4j
//public class SupportedProviderNormalizingOAuth2UserService extends DefaultOAuth2UserService {
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//        SupportedOAuth2Provider oAuth2Provider = EnumSet.allOf(SupportedOAuth2Provider.class).stream()
//                .filter(provider -> provider.providerId.equals(registrationId))
//                .findAny()
//                // TODO 서비스 익셉션 정의되면 그 익셉션 적용하기
//                .orElseThrow(() -> new IllegalArgumentException(String.format("%s는 지원하지 않는 OAuth2 제공자입니다.", registrationId)));
//        return new LuckyOAuth2User(oAuth2Provider, oAuth2Provider.normalize(oAuth2User.getAttributes()));
//    }
//}
