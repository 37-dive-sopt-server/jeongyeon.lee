package org.sopt.domain.article.repository;

import org.sopt.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleJpaRepository extends JpaRepository<Article, Long> {

    boolean existsByTitle(String title);

    @Query("select a from Article a order by a.createdAt desc")
    List<Article> findAllOrderByCreatedAt();
}
