package io.swyp.luckybackend.articles.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swyp.luckybackend.articles.dto.CreateArticleDto;
import io.swyp.luckybackend.common.ResponseDTO;
import io.swyp.luckybackend.articles.service.ArticleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
@Slf4j
public class ArticleController {
    private final ArticleService articleService;

    @Operation(summary = "게시글 쓰기")
    @PostMapping(value = "")
    public ResponseEntity<ResponseDTO> createArticle(HttpServletRequest request,
                                                     @RequestBody CreateArticleDto requestDto) {
        String token = request.getHeader("Authorization");
        return articleService.createArticle(token, requestDto);
    }
}
