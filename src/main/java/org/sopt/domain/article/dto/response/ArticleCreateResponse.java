package org.sopt.domain.article.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.article.entity.Article;

public record ArticleCreateResponse(
        @Schema(description = "회원 ID", example = "1")
        Long articleId
) {
    public static ArticleCreateResponse from(Article article) {
        return new ArticleCreateResponse(article.getId());
    }
}
