package org.sopt.domain.article.dto.response;

import org.sopt.domain.article.entity.Article;

public record ArticleCreateResponse(
        Long articleId
) {
    public static ArticleCreateResponse from(Article article) {
        return new ArticleCreateResponse(article.getId());
    }
}
