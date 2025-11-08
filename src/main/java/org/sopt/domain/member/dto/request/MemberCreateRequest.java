package org.sopt.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.sopt.domain.member.service.dto.request.MemberCreateCommand;
import org.sopt.global.annotation.ValidEmail;
import org.sopt.global.annotation.ValidGender;

public record MemberCreateRequest(
        @NotBlank(message = "이름은 필수 입력 사항입니다.")
        @Schema(description = "이름", example = "이정연")
        String name,

        @NotBlank(message = "생년월일은 필수 입력 사항입니다.")
        @Schema(description = "생년월일", example = "2000-11-12")
        String birthDate,

        @ValidEmail
        @Schema(description = "이메일", example = "dlwjddus1112@naver.com")
        String email,

        @ValidGender
        @Schema(description = "성별", example = "MALE")
        String gender
) {
        public MemberCreateCommand toCommand() {
            return new MemberCreateCommand(name, birthDate, email, gender);
        }
}
