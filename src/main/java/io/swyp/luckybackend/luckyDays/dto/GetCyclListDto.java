package io.swyp.luckybackend.luckyDays.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCyclListDto {
    private Long cyclNo;
    private Date startDt;
    private Date endDt;
}
