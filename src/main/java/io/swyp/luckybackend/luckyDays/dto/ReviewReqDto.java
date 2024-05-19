package io.swyp.luckybackend.luckyDays.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewReqDto {
    private Long dtlNo;
    private String review;
}


