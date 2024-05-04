package io.swyp.luckybackend.luckyDays.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetLcDayCyclDto {
    Date startDt;
    Date endDt;
    int period;
    int cnt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    List<LocalDate> expDtList = new ArrayList<>();


    public GetLcDayCyclDto(Date startDt, Date endDt, int period, int cnt, String exptDt) {
        this.startDt = startDt;
        this.endDt = endDt;
        this.period = period;
        this.cnt = cnt;
        parseDates(exptDt);
    }

    private void parseDates(String exptDt) {
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("UTC"));

        try {
            for(String dateStr : exptDt.split(",")) {
                LocalDate date = LocalDate.parse(dateStr.trim(), sdf);
                this.expDtList.add(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
