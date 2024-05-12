package io.swyp.luckybackend.luckyDays.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "LC_MSG")
public class LcMsgEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MSG_NO", nullable = false)
    private Long msgNo;

    @Column(name = "SJ", length = 100)
    private String sj;

    @Lob
    @Column(name = "CONTENT", columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(name = "VRIABL_WORDS", length = 50)
    private String variableWords;

    @Column(name = "VRIABL_CNT")
    private Integer variableCnt;

    @Column(name = "VRIABL_SYMBOL", length = 50)
    private String variableSymbol;

    @Column(name = "IMG", length = 200)
    private String img;

}
