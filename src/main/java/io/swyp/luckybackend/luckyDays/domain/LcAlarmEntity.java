package io.swyp.luckybackend.luckyDays.domain;

import io.swyp.luckybackend.users.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "LC_ALARM")
public class LcAlarmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ALARM_NO", nullable = false)
    private Long alarmNo;

    @Column(name = "ALARM_TY_CODE", length = 20)
    private String alarmTyCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_NO")
    private UserEntity user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DTL_NO")
    private LcDayDtlEntity dtl;

    @Column(name = "SJ", length = 50)
    private String sj;

    @Lob
    @Column(name = "CONTENT", columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(name = "SEND_YN", length = 1, nullable = false, columnDefinition = "CHAR DEFAULT 'N'")
    private char sendYn;

    @Column(name = "SEND_STATUS", length = 10)
    private String sendStatus;


    @Column(name = "D_DAY")
    private LocalDate dDay;

    @Column(name = "REG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date regDate;

    @Column(name = "UPD_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updDate;

    @Builder
    public LcAlarmEntity(String alarmTyCode, UserEntity user, LcDayDtlEntity dtl, String sj, String content, char sendYn, String sendStatus, LocalDate dDay) {
        this.alarmTyCode = alarmTyCode;
        this.user = user;
        this.dtl = dtl;
        this.sj = sj;
        this.content = content;
        this.sendYn = sendYn;
        this.sendStatus = sendStatus;
        this.dDay = dDay;
    }
}