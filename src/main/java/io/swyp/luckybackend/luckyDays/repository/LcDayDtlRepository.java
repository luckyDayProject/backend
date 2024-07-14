package io.swyp.luckybackend.luckyDays.repository;

import io.swyp.luckybackend.luckyDays.domain.LcDayDtlEntity;
import io.swyp.luckybackend.luckyDays.dto.CheckImgAndReviewDto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface LcDayDtlRepository extends JpaRepository<LcDayDtlEntity, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE LcDayDtlEntity e " +
            "SET e.imageName = :imageName, e.imagePath = :imagePath " +
            "WHERE e.dtlNo = :dtlNo")
    void insertImage(@Param("dtlNo") int dtlNo, @Param("imageName") String imageName, @Param("imagePath") String imagePath);

    @Modifying
    @Transactional
    @Query("UPDATE LcDayDtlEntity e " +
            "SET e.review = :review, e.imageName = :imageName, e.imagePath = :imagePath " +
            "WHERE e.dtlNo = :dtlNo " +
            "AND e.user.userNo = :userNo")
    void updateReview(@Param("dtlNo") Long dtlNo, @Param("review") String review, @Param("imageName") String imageName, @Param("imagePath") String imagePath, @Param("userNo") Long userNo);


    @Query("SELECT COUNT(u) > 0 " +
            "FROM LcDayDtlEntity u " +
            "WHERE u.dtlNo = :dtlNo " +
            "AND u.user.userNo = :userNo")
    boolean getUserNoByDtlNo(@Param("dtlNo") Long dtlNo, @Param("userNo") Long userNo);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM LcDayDtlEntity a " +
            "JOIN a.cycl b " +
            "WHERE a.user.userNo = :userNo AND a.dDay >= :today " +
            "AND b.reset = 'N'")
    boolean existsByUserNoAndDDayNotPassed(@Param("userNo") Long userNo, @Param("today") LocalDate today);

    @Query("SELECT a.category FROM LcDayDtlEntity d JOIN d.activity a WHERE d.dtlNo = :dtlNo")
    String findCategoryByDtlNo(@Param("dtlNo") Long dtlNo);


    CheckImgAndReviewDto findByDtlNo(@Param("dtlNo") Long dtlNo);
}
