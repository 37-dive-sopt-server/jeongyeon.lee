package org.sopt.domain.member.dto.response;

import org.sopt.domain.member.constant.Gender;
import org.sopt.domain.member.entity.Member;

import java.time.LocalDate;

public record MemberDetailResponse(
        Long memberId,
        String name,
        LocalDate birthDate,
        String email,
        Gender gender
) {
    public static MemberDetailResponse from(Member member) {
        return new MemberDetailResponse(member.getId(), member.getName(), member.getBirthDate(), member.getEmail(), member.getGender());
    }
}
