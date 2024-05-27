package io.swyp.luckybackend.luckyDays.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetLcDayDtlDto {
    private LocalDate dDay;
    private String actNm;
    private String actInfo;
    @Nullable
    private String review;
    @Nullable
    private String imageName;
    @Nullable
    private String imagePath;


}
