package org.sopt.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.sopt.domain.member.constant.Gender;
import org.sopt.domain.member.entity.Member;

import java.time.LocalDate;

public record MemberDetailResponse(
        @Schema(description = "회원 ID", example = "1")
        Long memberId,

        @Schema(description = "회원 이름", example = "이정연")
        String name,

        @Schema(description = "생년월일", example = "2000-11-12")
        LocalDate birthDate,

        @Schema(description = "이메일", example = "dlwjddus1112@naver.com")
        String email,

        @Schema(description = "성별", example = "MALE")
        Gender gender
) {
    public static MemberDetailResponse from(Member member) {
        return new MemberDetailResponse(member.getId(), member.getName(), member.getBirthDate(), member.getEmail(), member.getGender());
    }
}
