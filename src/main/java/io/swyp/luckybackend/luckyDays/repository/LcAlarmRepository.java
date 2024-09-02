package io.swyp.luckybackend.luckyDays.repository;

import io.swyp.luckybackend.luckyDays.domain.LcAlarmEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LcAlarmRepository extends JpaRepository<LcAlarmEntity, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE LcAlarmEntity la SET la.sendYn = 'Y' WHERE la.alarmNo = :alarmNo")
    void updateSendYn(Long alarmNo);
}
