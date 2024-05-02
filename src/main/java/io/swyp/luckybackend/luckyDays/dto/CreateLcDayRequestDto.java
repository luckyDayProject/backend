package io.swyp.luckybackend.luckyDays.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateLcDayRequestDto {
    private List<Integer> actList;
    private List<String> customActList;
    private int period;
    private int cnt;
    private List<LocalDate> expDTList;

    @JsonCreator
    public CreateLcDayRequestDto(
            @JsonProperty("actList") List<Integer> actList,
            @JsonProperty("customActList") List<String> customActList,
            @JsonProperty("period") int period,
            @JsonProperty("cnt") int cnt,
            @JsonProperty("expDTList") List<LocalDate> expDTList) {
        this.actList = actList;
        this.customActList = customActList;
        this.period = period;
        this.cnt = cnt;
        this.expDTList = expDTList;
    }



    @Override
    public String toString() {
        return "CreateLcDayRequestDto{" +
                "actList=" + actList +
                ", customActList=" + customActList +
                ", period=" + period +
                ", cnt=" + cnt +
                ", expDTList=" + expDTList +
                '}';
    }
}
