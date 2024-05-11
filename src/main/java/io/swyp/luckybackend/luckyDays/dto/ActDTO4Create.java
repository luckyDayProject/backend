package io.swyp.luckybackend.luckyDays.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ActDTO4Create {

    private Long actNo;
    private String activityName;
    @Builder
    public ActDTO4Create(Long actNo, String activityName) {
        this.actNo = actNo;
        this.activityName = activityName;
    }

    @Override
    public String toString() {
        return "ActDTO4Create{" +
                "actNo=" + actNo +
                ", activityName='" + activityName + '\'' +
                '}';
    }
}
