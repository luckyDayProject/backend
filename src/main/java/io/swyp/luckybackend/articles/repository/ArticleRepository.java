package io.swyp.luckybackend.articles.repository;

import io.swyp.luckybackend.articles.domain.LcArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<LcArticleEntity, Long> {
    LcArticleEntity findByArticleNo(long articleNo);
}
