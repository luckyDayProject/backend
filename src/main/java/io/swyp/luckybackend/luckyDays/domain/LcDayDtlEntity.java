package io.swyp.luckybackend.luckyDays.domain;

import io.swyp.luckybackend.users.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "LC_DAY_DTL")
public class LcDayDtlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "DTL_NO", nullable = false)
    private Long dtlNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CYCL_NO", referencedColumnName = "CYCL_NO", nullable = false)
    private LcDayCycleEntity cycl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO", referencedColumnName = "USER_NO", nullable = false)
    private UserEntity user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACTIVITY_NO", referencedColumnName = "ACTIVITY_NO", nullable = false)
    private LcActivityEntity activity;

    @Column(name = "ACTIVITY_NM", length = 50)
    private String activityNm;

    @Lob
    @Column(name = "REVIEW")
    private String review;

    @Column(name = "IMAGE", length = 200)
    private String image;

    @Column(name = "D_DAY")
    @Temporal(TemporalType.DATE)
    private Date dDay;

    @Column(name = "DTL_ORDER")
    private Integer dtlOrder;

    @OneToOne(mappedBy = "dtl", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private LcAlarmEntity alarm;

    @Builder
    public LcDayDtlEntity(Long dtlNo, LcDayCycleEntity cycl, UserEntity user, LcActivityEntity activity, String activityNm, String review, String image, Date dDay, Integer dtlOrder) {
        this.dtlNo = dtlNo;
        this.cycl = cycl;
        this.user = user;
        this.activity = activity;
        this.activityNm = activityNm;
        this.review = review;
        this.image = image;
        this.dDay = dDay;
        this.dtlOrder = dtlOrder;
    }
}
