package io.swyp.luckybackend.articles.dto;

import io.swyp.luckybackend.articles.domain.LcArticleEntity;
import io.swyp.luckybackend.users.domain.UserEntity;
import lombok.Getter;

@Getter
public class ModifyArticleDto {
    private Long articleNo;
    private String subject;
    private String content;

    public LcArticleEntity articleDto2entity(UserEntity user){
        return LcArticleEntity.builder()
                .subject(this.subject)
                .content(this.content)
                .user(user)
                .build();
    }

    @Override
    public String toString() {
        return "CreateArticleDto{" +
                "articleNo='" + articleNo + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
