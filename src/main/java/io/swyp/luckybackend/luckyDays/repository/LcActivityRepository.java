package io.swyp.luckybackend.luckyDays.repository;

import io.swyp.luckybackend.luckyDays.domain.LcActivityEntity;
import io.swyp.luckybackend.users.dto.GetActivityListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LcActivityRepository extends JpaRepository<LcActivityEntity, Long> {

    @Query("SELECT category, activityNo, keyword FROM LcActivityEntity")
    List<GetActivityListDto> getActivityList();
}
