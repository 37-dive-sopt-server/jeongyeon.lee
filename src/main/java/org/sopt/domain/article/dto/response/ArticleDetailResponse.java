package org.sopt.domain.article.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.article.entity.Article;

import java.time.LocalDateTime;

public record ArticleDetailResponse(
        @Schema(description = "아티클 ID", example = "1")
        Long articleId,

        @Schema(description = "작성자 ID", example = "1")
        Long memberId,

        @Schema(description = "작성자 이름", example = "이정연")
        String authorName,

        @Schema(description = "제목", example = "아")
        String title,

        @Schema(description = "내용", example = "과제하기싫다~")
        String content,

        @Schema(description = "태그", example = "CS")
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
