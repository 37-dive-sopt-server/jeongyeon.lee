package org.sopt.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.sopt.domain.comment.service.dto.request.CreateCommentCommand;

public record CreateCommentRequest(
        @Schema(description = "댓글 내용", example = "ㅎㅇㅎㅇ")
        @NotBlank(message = "댓글 내용은 필수입니다.")
        String content
) {
    public CreateCommentCommand toCommand() {
        return new CreateCommentCommand(content);
    }
}
