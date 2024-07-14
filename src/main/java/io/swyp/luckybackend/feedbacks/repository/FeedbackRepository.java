package io.swyp.luckybackend.feedbacks.repository;

import io.swyp.luckybackend.feedbacks.domain.FeedBackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedBackEntity, Long> {

}
