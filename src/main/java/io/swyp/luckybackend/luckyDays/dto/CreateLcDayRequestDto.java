package io.swyp.luckybackend.luckyDays.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class CreateLcDayRequestDto {
    private List<Integer> actList;
    private List<String> customActList;
    private int period;
    private int cnt;
    private List<Date> expDTList;

    @Builder
    public CreateLcDayRequestDto(List<Integer> actList, List<String> customActList, int period, int cnt, List<Date> expDTList) {
        this.actList = actList;
        this.customActList = customActList;
        this.period = period;
        this.cnt = cnt;
        this.expDTList = expDTList;
    }
}
