package io.swyp.luckybackend.luckyDays.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryActivitiesDto {
    private String category;
    private List<ActivityDto> actList;
}
