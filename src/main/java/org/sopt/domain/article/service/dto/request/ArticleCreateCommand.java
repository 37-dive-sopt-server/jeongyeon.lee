package org.sopt.domain.article.service.dto.request;

public record ArticleCreateCommand(
        Long memberId,

        String title,

        String content,

        String tag
) {
}
