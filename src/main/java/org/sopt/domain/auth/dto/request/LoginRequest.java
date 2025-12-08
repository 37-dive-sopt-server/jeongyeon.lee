package org.sopt.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginRequest(
        @Schema(description = "이메일", example = "dlwjddus1112@naver.com")
        @NotBlank(message = "이메일은 필수 입력 사항입니다.")
        String email,

        @Schema(description = "비밀번호", example = "wjddus11")
        @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
        String password
) {
}
