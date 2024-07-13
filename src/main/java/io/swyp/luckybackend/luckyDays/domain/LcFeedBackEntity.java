package io.swyp.luckybackend.luckyDays.domain;

import io.swyp.luckybackend.users.domain.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "LC_FEEDBACK")
public class LcFeedBackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FEEDBACK_NO", nullable = false)
    private Long feedbackNo;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_NO", referencedColumnName = "USER_NO", nullable = false)
    private UserEntity user;

    @Lob
    @Column(name = "CONTENT", columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(name = "REG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date regDate;
}
