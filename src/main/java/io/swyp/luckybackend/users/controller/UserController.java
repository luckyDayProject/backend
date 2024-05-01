package io.swyp.luckybackend.users.controller;

import io.swyp.luckybackend.users.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.http.HttpHeaders;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "사용자 API")
@Slf4j
public class UserController {
    private final UserService userServiceImpl;

    @Operation(
            summary = "로그인 API"
    )
    @GetMapping("/sign-in")
    public void login(HttpServletResponse response) throws Exception {
        log.info("로그인 API 진입");
        response.sendRedirect("/oauth2/authorization/kakao");
    }

    @Operation(
            summary = "회원 정보 조회 API"
    )
    @GetMapping("/")
    public void getUserInfo(HttpHeaders headers) throws Exception {
        log.info("회원 정보 조회 API 진입");
    }

    @Operation(
            summary = "회원 정보 수정 API"
    )
    @PutMapping("/")
    public void modifyUserInfo(HttpHeaders headers) throws Exception {
        log.info("회원 정보 수정 API 진입");
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

    @Operation(
            summary = "회원 탈퇴 API"
    )
    @DeleteMapping("/")
    public void withdrawUser(HttpHeaders headers) throws Exception {
        log.info("회원 탈퇴 API 진입");
    }


}
