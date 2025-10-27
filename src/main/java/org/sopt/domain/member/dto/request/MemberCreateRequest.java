package org.sopt.domain.member.dto.request;

public record MemberCreateRequest(
        String name,
        String birthDate,
        String email,
        String gender
) {
}
