package io.swyp.luckybackend.luckyDays.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetLcDayListDto {
    private long dtlNo;
    @Nullable
    private Integer dDay;
    @Nullable
    private Date date;
    private int order;
}
