package io.swyp.luckybackend.articles.repository;

import io.swyp.luckybackend.articles.domain.LcArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<LcArticleEntity, Long> {
}
