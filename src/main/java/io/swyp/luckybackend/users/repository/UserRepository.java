package io.swyp.luckybackend.users.repository;

import io.swyp.luckybackend.users.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserNo(Long userNo);

    boolean existsByOauthId(String oauthId);

    UserEntity findByOauthId(String oauthId);

    @Query("SELECT u.oauthId FROM UserEntity u WHERE u.userNo = :userNo")
    String findOauthIdByUserNo(@Param("userNo") Long userNo);

    @Query("SELECT u.isExp FROM UserEntity u WHERE u.userNo = :userNo")
    int findIsExpByUserNo(@Param("userNo") Long userNo);
}
