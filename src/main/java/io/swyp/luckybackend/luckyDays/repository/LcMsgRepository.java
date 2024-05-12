package io.swyp.luckybackend.luckyDays.repository;

import io.swyp.luckybackend.luckyDays.domain.LcMsgEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LcMsgRepository  extends JpaRepository<LcMsgEntity, Long> {
}
