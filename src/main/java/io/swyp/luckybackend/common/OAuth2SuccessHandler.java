package io.swyp.luckybackend.common;

import io.swyp.luckybackend.users.domain.CustomOAuth2User;
import io.swyp.luckybackend.users.domain.UserEntity;
import io.swyp.luckybackend.users.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final UserService userServiceImpl;
    @Value("${redirect-lucky-url}")
    String redirectUrl;

    @Value("${expiration-time}")
    String expirationTime;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        long userNo = oAuth2User.getUserNo();
//        Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
//        Map<String, String> profile = (Map<String, String>) kakaoAccount.get("profile");
//        String nickname = profile.get("nickname");
        String token = jwtProvider.create(userNo);
        UserEntity userEntity = userServiceImpl.getUserEntityByUserNo(userNo);
        String nickname = URLEncoder.encode(userEntity.getNickname(), StandardCharsets.UTF_8.name());
        response.sendRedirect(String.format("%s/%s/%s/%s", redirectUrl, token, expirationTime, nickname));
//        response.sendRedirect(String.format("%s/%s/3600", redirectUrl, token));
    }
}
