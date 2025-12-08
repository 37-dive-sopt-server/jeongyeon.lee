package org.sopt.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.sopt.domain.comment.entity.Comment;

import java.time.LocalDateTime;

@Builder
public record CommentResponse(
        @Schema(description = "댓글 ID", example = "1")
        Long commentId,

        @Schema(description = "작성자 ID", example = "1")
        Long writerId,

        @Schema(description = "댓글 내용", example = "ㅎㅇㅎㅇ")
        String content,

        @Schema(description = "생성 시간")
        LocalDateTime createdAt
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(comment.getId(),comment.getMember().getId(), comment.getContent(), comment.getCreatedAt());
    }
}
