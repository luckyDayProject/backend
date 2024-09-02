package io.swyp.luckybackend.luckyDays.repository;

import io.swyp.luckybackend.luckyDays.domain.LcAlarmEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LcAlarmRepository extends JpaRepository<LcAlarmEntity, Long> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE lc_alarm SET SEND_YN = 'Y' WHERE alarm_no = :alarmNo", nativeQuery = true)
    void updateSendYn(@Param("alarmNo") Long alarmNo);
}
