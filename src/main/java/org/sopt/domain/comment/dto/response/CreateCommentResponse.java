package org.sopt.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.comment.entity.Comment;

public record CreateCommentResponse(
        @Schema(description = "댓글 ID", example = "1")
        Long commentId
) {
    public static CreateCommentResponse from(Comment comment) {
        return new CreateCommentResponse(comment.getId());
    }
}
