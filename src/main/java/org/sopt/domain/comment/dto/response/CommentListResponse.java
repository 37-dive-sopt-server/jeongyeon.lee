package org.sopt.domain.comment.dto.response;

import org.sopt.domain.comment.entity.Comment;

import java.util.List;

public record CommentListResponse(
        List<CommentResponse> commentResponses
) {
    public static CommentListResponse from(List<Comment> comments) {
        return new CommentListResponse(comments.stream()
                .map(CommentResponse::from)
                .toList()
        );
    }
}
