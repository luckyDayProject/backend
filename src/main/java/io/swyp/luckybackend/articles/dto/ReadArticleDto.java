package io.swyp.luckybackend.articles.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReadArticleDto {
    private Long articleNo;
    private String subject;
    private String content;

    @Builder
    public ReadArticleDto(Long articleNo, String subject, String content) {
        this.articleNo = articleNo;
        this.subject = subject;
        this.content = content;
    }
}
