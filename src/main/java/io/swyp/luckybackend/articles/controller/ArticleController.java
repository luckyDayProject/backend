package io.swyp.luckybackend.articles.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swyp.luckybackend.articles.dto.CreateArticleDto;
import io.swyp.luckybackend.articles.dto.ModifyArticleDto;
import io.swyp.luckybackend.common.ResponseDTO;
import io.swyp.luckybackend.articles.service.ArticleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "게시글 보기")
    @GetMapping(value = "")
    public ResponseEntity<ResponseDTO> readArticle(HttpServletRequest request,
                                                     @RequestParam long articleNo) {
        return articleService.readArticle(articleNo);
    }

    @Operation(summary = "게시글 수정")
    @PutMapping(value = "")
    public ResponseEntity<ResponseDTO> modifyArticle(HttpServletRequest request,
                                                     @RequestBody ModifyArticleDto requestDto) {
        String token = request.getHeader("Authorization");
        return articleService.modifyArticle(token, requestDto);
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping(value = "")
    public ResponseEntity<ResponseDTO> deleteArticle(HttpServletRequest request,
                                                     @RequestParam long articleNo) {
        String token = request.getHeader("Authorization");
        return articleService.deleteArticle(token, articleNo);
    }
}
