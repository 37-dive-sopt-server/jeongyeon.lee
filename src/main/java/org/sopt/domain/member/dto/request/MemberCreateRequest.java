package org.sopt.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.sopt.domain.member.service.dto.request.MemberCreateCommand;
import org.sopt.global.annotation.ValidEmail;
import org.sopt.global.annotation.ValidGender;

public record MemberCreateRequest(
        @NotBlank(message = "이름은 필수 입력 사항입니다.")
        String name,

        @NotBlank(message = "생년월일은 필수 입력 사항입니다.")
        String birthDate,

        @ValidEmail
        String email,

        @ValidGender
        String gender
) {
        public MemberCreateCommand toCommand() {
            return new MemberCreateCommand(name, birthDate, email, gender);
        }
}
