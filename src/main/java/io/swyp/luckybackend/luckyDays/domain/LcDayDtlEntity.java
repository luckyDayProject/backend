package io.swyp.luckybackend.luckyDays.domain;

import io.swyp.luckybackend.users.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "LC_DAY_DTL")
public class LcDayDtlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DTL_NO", nullable = false)
    private Long dtlNo;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CYCL_NO", referencedColumnName = "CYCL_NO", nullable = false)
    private LcDayCycleEntity cycl;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_NO", referencedColumnName = "USER_NO", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACTIVITY_NO", referencedColumnName = "ACTIVITY_NO", nullable = false)
    private LcActivityEntity activity;

    @Column(name = "ACTIVITY_NM", length = 50)
    private String activityNm;

    @Lob
    @Column(name = "REVIEW")
    private String review;

    @Column(name = "IMAGE_NAME", length = 200)
    private String imageName;

    @Column(name = "IMAGE_PATH", length = 200)
    private String imagePath;

    @Column(name = "D_DAY")
    @Temporal(TemporalType.DATE)
    private LocalDate dDay;

    @Column(name = "DTL_ORDER")
    private Integer dtlOrder;

    @OneToOne(mappedBy = "dtl", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private LcAlarmEntity alarm;

    @Builder
    public LcDayDtlEntity(Long dtlNo, LcDayCycleEntity cycl, UserEntity user, LcActivityEntity activity, String activityNm, String review, String imageName, String imagePath, LocalDate dDay, Integer dtlOrder) {
        this.dtlNo = dtlNo;
        this.cycl = cycl;
        this.user = user;
        this.activity = activity;
        this.activityNm = activityNm;
        this.review = review;
        this.imageName = imageName;
        this.imagePath = imagePath;
        this.dDay = dDay;
        this.dtlOrder = dtlOrder;
    }
}
