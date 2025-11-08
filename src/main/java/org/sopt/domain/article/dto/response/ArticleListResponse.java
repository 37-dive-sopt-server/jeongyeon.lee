package org.sopt.domain.article.dto.response;

import org.sopt.domain.article.entity.Article;

import java.util.List;

public record ArticleListResponse(
        List<ArticleDetailResponse> articles
) {
    public static ArticleListResponse from(List<Article> articles) {
        return new ArticleListResponse(articles.stream()
                .map(ArticleDetailResponse::from)
                .toList());
    }
}
