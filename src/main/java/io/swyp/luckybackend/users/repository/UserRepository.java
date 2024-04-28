package io.swyp.luckybackend.users.repository;

import io.swyp.luckybackend.users.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserNo(Long userNo);
}
