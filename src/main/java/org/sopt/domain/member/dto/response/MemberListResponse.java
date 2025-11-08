package org.sopt.domain.member.dto.response;

import java.util.List;

public record MemberListResponse(
        List<MemberDetailResponse> members
) {
}
