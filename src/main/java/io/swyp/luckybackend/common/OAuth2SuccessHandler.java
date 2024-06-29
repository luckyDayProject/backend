package io.swyp.luckybackend.common;

import io.swyp.luckybackend.luckyDays.repository.LcDayCycleRepository;
import io.swyp.luckybackend.luckyDays.repository.LcDayDtlRepository;
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
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final UserService userServiceImpl;

    private final LcDayDtlRepository lcDayDtlRepository;
    private final LcDayCycleRepository lcDayCycleRepository;

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
        UserEntity userEntity = userServiceImpl.getUserEntityByOauthId(Long.toString(userNo));
        String token = jwtProvider.create(userEntity.getUserNo());
        String nickname = URLEncoder.encode(userEntity.getNickname(), StandardCharsets.UTF_8.name());
        String email = URLEncoder.encode(userEntity.getEmail(), StandardCharsets.UTF_8.name());
        int isExistLcDay = lcDayDtlRepository.existsByUserNoAndDDayNotPassed(userNo, LocalDate.now()) ? 1 : 0;
//        int isExp = lcDayCycleRepository.existsByUserUserNo(userNo) ? 1 : 0;
        int isExp = userServiceImpl.getUserIsExp(userEntity.getUserNo());
//        response.sendRedirect(String.format("%s/%s/%s/%s", redirectUrl, token, expirationTime, nickname));
        response.sendRedirect(String.format("%s?token=%s&expirationTime=%s&nickname=%s&email=%s&isExistLcDay=%s&isExp=%s&prfNo=%s",
                redirectUrl, token, expirationTime, nickname, email, isExistLcDay, isExp, userEntity.getProfileIconNo()));
    }
}
