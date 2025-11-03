package org.sopt.domain.member.service.dto.request;

public record MemberCreateCommand(
        String name,

        String birthDate,

        String email,

        String gender
) {
}
