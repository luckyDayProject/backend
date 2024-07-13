package io.swyp.luckybackend.articles.domain;

import io.swyp.luckybackend.users.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "LC_ARTICLE")
public class LcArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ARTICLE_NO", nullable = false)
    private Long articleNo;

    @Column(name = "CATEGORY", length = 50)
    private String category;
    @Column(name = "SUBJECT", length = 50)
    private String subject;
    @Lob
    @Column(name = "CONTENT", columnDefinition = "MEDIUMTEXT")
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO")
    private UserEntity user;
    @Column(name = "VIEW_CNT")
    private int view_cnt;
    @Column(name = "REG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date regDate;

    @Column(name = "UPD_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updDate;

    @Builder
    public LcArticleEntity(String category, String subject, String content, UserEntity user) {
        this.category = category;
        this.subject = subject;
        this.content = content;
        this.user = user;
    }
}
