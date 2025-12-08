package org.sopt.domain.comment.service.dto.request;

import lombok.Builder;

@Builder
public record CommentCommand(
        String content
) {
}
