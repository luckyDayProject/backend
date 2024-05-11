package io.swyp.luckybackend.luckyDays.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCyclListDto {
    private Long cyclNo;
    private Date startDt;
    private Date endDt;

    public String getStartDt() {
        return formatIsoDate(startDt.toString());
    }

    public String getEndDt() {
        return formatIsoDate(endDt.toString());
    }

    private String formatIsoDate(String isoDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        try {
            Date date = inputFormat.parse(isoDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
