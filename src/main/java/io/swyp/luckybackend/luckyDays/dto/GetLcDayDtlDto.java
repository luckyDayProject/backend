package io.swyp.luckybackend.luckyDays.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetLcDayDtlDto {
    private Date dDay;
    private String actNm;
    private String actInfo;
    @Nullable
    private String review;
    @Nullable
    private String image;

}
