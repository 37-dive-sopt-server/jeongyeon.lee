package org.sopt.domain.member.service.dto.request;

import lombok.Builder;

@Builder
public record MemberCreateCommand(
        String name,

        String password,

        String birthDate,

        String email,

        String gender
) {
}
