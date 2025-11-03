package org.sopt.domain.article.dto.response;

import org.sopt.domain.article.entity.Article;

import java.time.LocalDateTime;

public record ArticleDetailResponse(
        Long articleId,
        Long memberId,
        String authorName,
        String title,
        String content,
        String tag,
        LocalDateTime createdAt
) {
    public static ArticleDetailResponse from(Article article){
        return new ArticleDetailResponse(
                article.getId(),
                article.getMember().getId(),
                article.getMember().getName(),
                article.getTitle(),
                article.getContent(),
                article.getTag().toString(),
                article.getCreatedAt());
    }
}
