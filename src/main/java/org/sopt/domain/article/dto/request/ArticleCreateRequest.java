package org.sopt.domain.article.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.sopt.domain.article.service.dto.request.ArticleCreateCommand;
import org.sopt.global.annotation.ValidTag;

public record ArticleCreateRequest(
        @NotNull
        Long memberId,

        @NotBlank(message = "제목은 비어있을 수 없습니다.")
        String title,

        @NotBlank(message = "내용은 비어있을 수 없습니다.")
        String content,

        @ValidTag
        String tag
) {
    public ArticleCreateCommand toCommand(){
            return new ArticleCreateCommand(memberId, title, content, tag);
    }
}
