package org.sopt.domain.article.service.dto.request;

import lombok.Builder;

@Builder
public record ArticleCreateCommand(

        String title,

        String content,

        String tag
) {
}
