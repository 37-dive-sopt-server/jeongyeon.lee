package org.sopt.domain.article.dto.response;

import org.sopt.domain.article.entity.Article;

import java.time.LocalDateTime;

public record ArticleDetailResponse(
        Long memberId,
        String title,
        String content,
        String tag,
        LocalDateTime createdAt
) {
    public static ArticleDetailResponse from(Article article){
        return new ArticleDetailResponse(article.getMember().getId(),
                article.getTitle(),
                article.getContent(),
                article.getTag().toString(),
                article.getCreatedAt());
    }
}
