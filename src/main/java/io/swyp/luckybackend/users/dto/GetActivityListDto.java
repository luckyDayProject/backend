package io.swyp.luckybackend.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetActivityListDto {
    private String category;
    private Long activityNo;
    private String keyword;
}
