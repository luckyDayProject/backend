package io.swyp.luckybackend.users.controller;

import io.jsonwebtoken.Jwts;
import io.swyp.luckybackend.common.JwtProvider;
import io.swyp.luckybackend.common.ResponseDTO;
import io.swyp.luckybackend.users.domain.UserEntity;
import io.swyp.luckybackend.users.dto.SignInRequestDto;
import io.swyp.luckybackend.users.dto.SignInResponseDto;
import io.swyp.luckybackend.users.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "사용자 API")
@Slf4j
public class UserController {
    private final UserService userServiceImpl;
    private final JwtProvider jwtProvider;

    @Operation(
            summary = "로그인 API"
    )
    @GetMapping("/sign-in")
    public void login(HttpServletResponse response) throws Exception {
        log.info("로그인 API 진입");
        response.sendRedirect("/oauth2/authorization/kakao");
    }

    @Value("${kakao.client-id}")
    private String clientId;

    @Value(("${logout-redirect-uri}"))
    private String logoutRedirectUri;
    @Operation(
            summary = "로그아웃 API"
    )
    @GetMapping("/sign-out")
    public void logout(HttpServletResponse response) throws Exception {
        log.info("로그아웃 API 진입");
        String url = String.format("https://kauth.kakao.com/oauth/logout?client_id=%s&logout_redirect_uri=%s"
        , clientId, logoutRedirectUri);
        response.sendRedirect(url);
    }



    @GetMapping("/getToken")
    public @ResponseBody String kakaoCallback(String code) { // Data를 리턴해주는 컨트롤러 함수

        log.info("토큰 API 진입");
        // POST 방식으로 key=value 데이터를 요청 (카카오쪽으로)
        // 이 때 필요한 라이브러리가 RestTemplate, 얘를 쓰면 http 요청을 편하게 할 수 있다.
        RestTemplate rt = new RestTemplate();

        // HTTP POST를 요청할 때 보내는 데이터(body)를 설명해주는 헤더도 만들어 같이 보내줘야 한다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // body 데이터를 담을 오브젝트인 MultiValueMap를 만들어보자
        // body는 보통 key, value의 쌍으로 이루어지기 때문에 자바에서 제공해주는 MultiValueMap 타입을 사용한다.
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "d6a322a67b89972c512e37af8d4f3769");
        params.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");
        params.add("code", code);

        // 요청하기 위해 헤더(Header)와 데이터(Body)를 합친다.
        // kakaoTokenRequest는 데이터(Body)와 헤더(Header)를 Entity가 된다.
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // POST 방식으로 Http 요청한다. 그리고 response 변수의 응답 받는다.
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token", // https://{요청할 서버 주소}
                HttpMethod.POST, // 요청할 방식
                kakaoTokenRequest, // 요청할 때 보낼 데이터
                String.class // 요청 시 반환되는 데이터 타입
        );
        return "카카오 토큰 요청 완료 : 토큰 요청에 대한 응답 : " + response;
    }

    @Operation(
            summary = "로그인 API"
    )
    @GetMapping("/test")
    public String test(HttpServletResponse response) throws Exception {
        log.info("로그인 API 진입");
        return "로그인 API";
    }
}
