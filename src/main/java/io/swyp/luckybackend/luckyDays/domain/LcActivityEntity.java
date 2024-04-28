package io.swyp.luckybackend.luckyDays.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "LC_ACTIVITY")
public class LcActivityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACTIVITY_NO", nullable = false)
    private Long activityNo;

    @Column(name = "CATEGORY", length = 50)
    private String category;

    @Column(name = "KEYWORD", length = 50)
    private String keyword;

    @Column(name = "ACTIVITY_NM", length = 50)
    private String activityName;

    @Lob
    @Column(name = "ACTIVITY_INFO")
    private String activityInfo;

    @Column(name = "USE_AT", length = 1)
    private char useAt;

    @Column(name = "PHOTO", length = 200)
    private String photo;

    @Builder
    public LcActivityEntity(Long activityNo, String category, String keyword, String activityName, String activityInfo, char useAt, String photo) {
        this.activityNo = activityNo;
        this.category = category;
        this.keyword = keyword;
        this.activityName = activityName;
        this.activityInfo = activityInfo;
        this.useAt = useAt;
        this.photo = photo;
    }
}
