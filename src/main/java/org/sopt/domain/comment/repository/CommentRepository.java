package org.sopt.domain.comment.repository;

import org.sopt.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndArticleId(Long id, Long articleId);
}
