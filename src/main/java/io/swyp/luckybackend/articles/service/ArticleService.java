package io.swyp.luckybackend.articles.service;

import io.swyp.luckybackend.articles.domain.LcArticleEntity;
import io.swyp.luckybackend.articles.dto.CreateArticleDto;
import io.swyp.luckybackend.articles.dto.ModifyArticleDto;
import io.swyp.luckybackend.articles.dto.ReadArticleDto;
import io.swyp.luckybackend.articles.repository.ArticleRepository;
import io.swyp.luckybackend.common.JwtProvider;
import io.swyp.luckybackend.common.ResponseDTO;
import io.swyp.luckybackend.users.domain.UserEntity;
import io.swyp.luckybackend.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public ResponseEntity<ResponseDTO> createArticle(String token, CreateArticleDto requestDto) {
        long userNo = getUserNo(token);
        UserEntity user = userRepository.findByUserNo(userNo);

//        카테고리가 공지사항(notice)인 경우는 admin 권한을 가진 유저만 게시글 생성 가능
//        if (requestDto.getCategory().equals("notice")&&!user.getRole("admin")){
//            return ResponseDTO.validationFail();
//        }
        LcArticleEntity article = requestDto.articleDto2entity(user);
        articleRepository.save(article);
        return ResponseDTO.success();
    }

    @Transactional
    public ResponseEntity<ResponseDTO> readArticle(long articleNo) {
        LcArticleEntity article = articleRepository.findByArticleNo(articleNo);
        ReadArticleDto readArticleDto = ReadArticleDto.builder()
                .articleNo(article.getArticleNo())
                .subject(article.getSubject())
                .content(article.getContent())
                .build();
        return ResponseDTO.success(readArticleDto);
    }

    @Transactional
    public ResponseEntity<ResponseDTO> modifyArticle(String token, ModifyArticleDto requestDto) {
        long userNo = getUserNo(token);
        UserEntity user = userRepository.findByUserNo(userNo);
        return ResponseDTO.success();
    }

    @Transactional
    public ResponseEntity<ResponseDTO> deleteArticle(String token, long articleNo) {
        long userNo = getUserNo(token);
        UserEntity user = userRepository.findByUserNo(userNo);
        return ResponseDTO.success();
    }

    public long getUserNo(String token) {
        return jwtProvider.getUserNo(token.substring(7));
    }
}
