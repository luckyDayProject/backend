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
            "AND a.cycl.cyclNo = (SELECT MAX(c.cycl.cyclNo) FROM LcDayDtlEntity c WHERE c.user.userNo = :userNo) " +
            "AND b.reset = 'N'")
    List<GetLcDayListDto> getLcDayList(@Param("userNo")long userNo, @Param("today")LocalDate today);

    @Query("SELECT new io.swyp.luckybackend.luckyDays.dto.GetLcDayListDto(a.dtlNo, a.cycl.cyclNo, FUNCTION('DATEDIFF', a.dDay, :today), a.dDay, a.dtlOrder) " +
            "FROM LcDayDtlEntity a " +
            "JOIN a.cycl b " +
            "WHERE a.dDay < :today " +
            "AND a.user.userNo = :userNo " +
            "AND a.cycl.cyclNo = (SELECT MAX(c.cycl.cyclNo) FROM LcDayDtlEntity c WHERE c.user.userNo = :userNo) " + // today가 endDate보다 전에 있는 조건을 추가
            "AND b.reset = 'N'")
    List<GetLcDayListDto> getPastLcDayList(@Param("userNo")long userNo, @Param("today")LocalDate today);

    @Query("SELECT new io.swyp.luckybackend.luckyDays.dto.GetLcDayListDto( a.dtlNo, a.cycl.cyclNo, FUNCTION('DATEDIFF', a.dDay, :today), a.dDay, a.dtlOrder) " +
            "FROM LcDayDtlEntity a " +
            "JOIN a.cycl b " +
            "WHERE a.dDay < :today " +
            "AND a.user.userNo = :userNo " +
            "AND a.cycl.cyclNo = :cyclNo " +
            "AND b.reset = 'N'")
    List<GetLcDayListDto> getLcDayListByHist(@Param("userNo")long userNo, @Param("cyclNo")Long cyclNo, @Param("today")LocalDate today);

    @Query("SELECT new io.swyp.luckybackend.luckyDays.dto.GetLcDayDtlDto(a.dDay, a.activityNm, b.activityInfo, a.review, a.imageName, a.imagePath) " +
            "FROM LcDayDtlEntity a " +
            "JOIN a.activity b " +
            "WHERE a.dtlNo = :dtlNo")
    GetLcDayDtlDto getLcDayDetail(int dtlNo);

    @Query("SELECT new io.swyp.luckybackend.luckyDays.dto.GetLcDayCyclDto(a.startDt, a.endDt, a.period, a.count, a.exptDt)" +
            "FROM LcDayCycleEntity a " +
            "WHERE a.cyclNo = :cyclNo " +
            "AND a.reset = 'N'")
    GetLcDayCyclDto getLcDayCyclInfo(@Param("cyclNo") int cyclNo);

    @Query("SELECT MAX(cyclNo) " +
            "FROM LcDayCycleEntity " +
            "WHERE user.userNo = :userNo " +
            "AND reset = 'N'")
    Long findCyclNo(long userNo);

    @Modifying
    @Transactional
    @Query("UPDATE LcDayCycleEntity " +
            "SET reset = 'Y' " +
            "WHERE cyclNo = (SELECT MAX(cyclNo) FROM LcDayCycleEntity WHERE user.userNo = :userNo)")
    void deleteLcDayCycl(@Param("userNo") long userNo);


    @Query("SELECT new io.swyp.luckybackend.luckyDays.dto.GetCyclListDto(a.cyclNo, a.startDt, a.endDt) " +
            "FROM LcDayCycleEntity a " +
            "WHERE a.user.userNo = :userNo")    // reset 조건 추가, 현재 진행중인 싸이클은 보이지 않게 처리
    List<GetCyclListDto> getLcDayCyclList(@Param("userNo") long userNo);

    @Query("SELECT a.activityName FROM LcActivityEntity a WHERE a.activityNo = :activityNo")
    String findActivityNameByActivityNo(@Param("activityNo") long activityNo);

    @Query("SELECT new io.swyp.luckybackend.luckyDays.dto.SendMailDto(" +
            "b.email, b.nickname, a.sj, a.content) " +
            "FROM LcAlarmEntity a " +
            "JOIN a.user b " +
            "WHERE a.dDay = :today")
    List<SendMailDto> getLcDay(LocalDate today);


}


