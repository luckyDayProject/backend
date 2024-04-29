package io.swyp.luckybackend.luckyDays.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryActivitiesDTO {
    private String category;
    private List<ActivityDTO> actList;
}
