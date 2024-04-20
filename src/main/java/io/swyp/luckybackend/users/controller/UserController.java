package io.swyp.luckybackend.users.controller;

import io.swyp.luckybackend.users.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "사용자 API")
@Slf4j
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "로그인 API"
    )
    @GetMapping("/login")
    public String login(HttpServletResponse response) throws Exception {
        log.info("로그인 API 진입");
        response.sendRedirect("/oauth2/authorization/kakao");
        return "로그인 API";
    }

    @Operation(
            summary = "로그아웃 API"
    )
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) throws Exception {
        log.info("로그아웃 API 진입");
        response.sendRedirect("/v1/user/logout");
        return "로그아웃 API";
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
        params.add("client_id", "e054053c94984f87d0f21b0abe71d10e");
        params.add("redirect_uri", "http://localhost:8090/auth/kakao/callback");
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
        return "카카오 토큰 요청 완료 : 토큰 요청에 대한 응답 : "+response;
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
