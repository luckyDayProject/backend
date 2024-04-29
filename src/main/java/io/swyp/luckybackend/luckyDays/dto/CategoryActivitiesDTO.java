package io.swyp.luckybackend.luckyDays.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CategoryActivitiesDTO {
    private String category;
    private List<ActivityDTO> actList;
}
