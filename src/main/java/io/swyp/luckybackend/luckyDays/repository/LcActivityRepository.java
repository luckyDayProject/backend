package io.swyp.luckybackend.luckyDays.repository;

import io.swyp.luckybackend.luckyDays.domain.LcActivityEntity;
import io.swyp.luckybackend.luckyDays.dto.*;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LcActivityRepository extends JpaRepository<LcActivityEntity, Long> {

    @Query("SELECT new io.swyp.luckybackend.luckyDays.dto.GetActivityListDto(a.category, a.activityNo, a.keyword) FROM LcActivityEntity a")
    List<GetActivityListDto> getActivityList();

    @Query("SELECT new io.swyp.luckybackend.luckyDays.dto.GetLcDayListDto(a.dtlNo, a.cycl.cyclNo, FUNCTION('DATEDIFF', a.dDay, :today), a.dDay, a.dtlOrder) " +
            "FROM LcDayDtlEntity a " +
            "JOIN a.cycl b " +
            "WHERE a.dDay >= :today " +
            "AND a.user.userNo = :userNo " +
            "AND a.cycl.cyclNo = :cyclNo " +
            "AND b.reset = 'N'")
    List<GetLcDayListDto> getLcDayList(long userNo, long cyclNo, LocalDate today);

    @Query("SELECT new io.swyp.luckybackend.luckyDays.dto.GetLcDayListDto( a.dtlNo, a.cycl.cyclNo, FUNCTION('DATEDIFF', a.dDay, :today), a.dDay, a.dtlOrder) " +
            "FROM LcDayDtlEntity a " +
            "JOIN a.cycl b " +
            "WHERE a.dDay < :today " +
            "AND a.user.userNo = :userNo " +
            "AND a.cycl.cyclNo = :cyclNo " +
            "AND b.reset = 'N'")
    List<GetLcDayListDto> getLcDayListByHist(long userNo, long cyclNo, LocalDate today);

    @Query("SELECT new io.swyp.luckybackend.luckyDays.dto.GetLcDayDtlDto(a.dDay, a.activityNm, b.activityInfo, a.review, a.imageName, a.imagePath) " +
            "FROM LcDayDtlEntity a " +
            "JOIN a.activity b " +
            "WHERE a.dtlNo = :dtlNo")
    GetLcDayDtlDto getLcDayDetail(int dtlNo);

    @Query("SELECT new io.swyp.luckybackend.luckyDays.dto.GetLcDayCyclDto(a.startDt, a.endDt, a.period, a.count, a.exptDt)" +
            "FROM LcDayCycleEntity a " +
            "WHERE a.cyclNo = :cyclNo")
    GetLcDayCyclDto getLcDayCyclInfo(int cyclNo);

    @Modifying
    @Transactional
    @Query("UPDATE LcDayCycleEntity SET reset = 'Y' WHERE cyclNo = (SELECT MAX(cyclNo) FROM LcDayCycleEntity WHERE user.userNo = :userNo)")
    void deleteLcDayCycl(@Param("userNo") long userNo);

//    void getLcDay(LocalDate today);

}
