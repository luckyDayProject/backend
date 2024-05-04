package io.swyp.luckybackend.luckyDays.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetLcDayListDto {
    private long dtlNo;
    private long cyclNo;
    @Nullable
    private Integer dDay;
    @Nullable
    private LocalDate date;
    private int order;
}
