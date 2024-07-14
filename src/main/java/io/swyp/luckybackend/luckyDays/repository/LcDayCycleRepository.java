package io.swyp.luckybackend.luckyDays.repository;

import io.swyp.luckybackend.luckyDays.domain.LcDayCycleEntity;
import io.swyp.luckybackend.users.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LcDayCycleRepository extends JpaRepository<LcDayCycleEntity, Long> {
    boolean existsByUserUserNo(long userNo);

    @Query("SELECT MAX(cyclNo) " +
            "FROM LcDayCycleEntity " +
            "WHERE user.userNo = :userNo " +
            "AND reset = 'N'")
    Long findCyclNo(@Param("userNo") long userNo);

    LcDayCycleEntity findTopByUserAndResetOrderByCyclNoDesc(UserEntity user, String reset);
}
