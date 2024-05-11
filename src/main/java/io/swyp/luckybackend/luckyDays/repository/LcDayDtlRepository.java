package io.swyp.luckybackend.luckyDays.repository;

import io.swyp.luckybackend.luckyDays.domain.LcDayDtlEntity;
import io.swyp.luckybackend.luckyDays.dto.ReviewReqDto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LcDayDtlRepository extends JpaRepository<LcDayDtlEntity, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE LcDayDtlEntity e " +
            "SET e.imageName = :imageName, e.imagePath = :imagePath " +
            "WHERE e.dtlNo = :dtlNo")
    void insertImage(int dtlNo, String imageName, String imagePath);

    @Modifying
    @Transactional
    @Query("UPDATE LcDayDtlEntity e " +
            "SET e.review = :review, e.imageName = :imageName, e.imagePath = :imagePath " +
            "WHERE e.dtlNo = :dtlNo")
    void insertReview(@Param("dtlNo") Long dtlNo, @Param("review") String review, @Param("imageName") String imageName, @Param("imagePath") String imagePath);
}
