package io.swyp.luckybackend.luckyDays.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "LC_MSG")
public class LcMsgEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MSG_NO", nullable = false)
    private Long msgNo;

    @Column(name = "SJ", length = 50)
    private String sj;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @Column(name = "VRIABL_WORDS", length = 50)
    private String variableWords;

    @Column(name = "VRIABL_CNT")
    private Integer variableCnt;

    @Column(name = "VRIABL_SYMBOL", length = 50)
    private String variableSymbol;

    @Builder
    public LcMsgEntity(Long msgNo, String sj, String content, String variableWords, Integer variableCnt, String variableSymbol) {
        this.msgNo = msgNo;
        this.sj = sj;
        this.content = content;
        this.variableWords = variableWords;
        this.variableCnt = variableCnt;
        this.variableSymbol = variableSymbol;
    }
}
