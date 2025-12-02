package org.sopt.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.comment.entity.Comment;

public record CommentResponse(
        @Schema(description = "댓글 ID", example = "1")
        Long commentId,

        @Schema(description = "댓글 내용", example = "ㅎㅇㅎㅇ")
        String content
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getContent());
    }
}
