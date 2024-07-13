package io.swyp.luckybackend.articles.dto;

import io.swyp.luckybackend.articles.domain.LcArticleEntity;
import io.swyp.luckybackend.users.domain.UserEntity;
import lombok.Getter;

@Getter
public class CreateArticleDto {
    private String category;
    private String subject;
    private String content;

    public LcArticleEntity articleDto2entity(UserEntity user){
        return LcArticleEntity.builder()
                .category(this.getCategory())
                .subject(this.getSubject())
                .content(this.getContent())
                .user(user)
                .build();
    }

    @Override
    public String toString() {
        return "CreateArticleDto{" +
                "category='" + category + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
