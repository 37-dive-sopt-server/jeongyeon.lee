package org.sopt.domain.article.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.sopt.domain.article.service.dto.request.ArticleCreateCommand;
import org.sopt.global.annotation.ValidTag;

public record ArticleCreateRequest(
        @NotNull
        @Schema(description = "회원 ID" ,example = "1")
        Long memberId,

        @NotBlank(message = "제목은 비어있을 수 없습니다.")
        @Schema(description = "제목", example = "아")
        String title,

        @NotBlank(message = "내용은 비어있을 수 없습니다.")
        @Schema(description = "내용", example = "과제하기 싫다~")
        String content,

        @ValidTag
        @Schema(description = "태그", example = "CS")
        String tag
) {
    public ArticleCreateCommand toCommand(){
            return new ArticleCreateCommand(memberId, title, content, tag);
    }
}
