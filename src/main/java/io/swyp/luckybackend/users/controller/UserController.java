package io.swyp.luckybackend.users.controller;

import io.swyp.luckybackend.users.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

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
            summary = "로그인 API"
    )
    @GetMapping("/test")
    public String test(HttpServletResponse response) throws Exception {
        log.info("로그인 API 진입");
        return "로그인 API";
    }
}
