package io.swyp.luckybackend.users.domain;

import io.swyp.luckybackend.luckyDays.domain.LcAlarmEntity;
import io.swyp.luckybackend.luckyDays.domain.LcDayCycleEntity;
import io.swyp.luckybackend.luckyDays.domain.LcDayDtlEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LC_USER")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_NO")
    private Long userNo;

    @Column(name = "OAUTH_ID")
    private String oauthId;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "GENDER")
    @Nullable
    private char gender;

    @Column(name = "AGE_GROUP")
    @Nullable
    private int ageGroup;

    @Column(name = "BIRTH_YEAR")
    @Nullable
    private int birthYear;

    @Column(name = "PRF_ICON_NO")
    private int profileIconNo;

    @Column(name = "REG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date regDate;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LcDayCycleEntity> cycles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LcDayDtlEntity> dtls = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LcAlarmEntity> alarms = new ArrayList<>();


    @Builder
    public UserEntity(String oauthId, String nickname, String email, char gender, int ageGroup, int birthYear, int profileIconNo) {
        this.oauthId = oauthId;
        this.nickname = nickname;
        this.email = email;
        this.gender = gender;
        this.ageGroup = ageGroup;
        this.birthYear = birthYear;
        this.profileIconNo = profileIconNo;
    }


    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeEmail(String email) {
        this.email = email;
    }
}
