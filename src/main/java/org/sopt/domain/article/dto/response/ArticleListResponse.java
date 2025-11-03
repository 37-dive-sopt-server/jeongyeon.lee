package org.sopt.domain.article.dto.response;

import java.util.List;

public record ArticleListResponse(
        List<ArticleDetailResponse> articles
) {
}
