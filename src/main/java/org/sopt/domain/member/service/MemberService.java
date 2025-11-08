package org.sopt.domain.member.service;

import org.sopt.domain.member.dto.response.MemberDetailResponse;
import org.sopt.domain.member.dto.response.MemberListResponse;
import org.sopt.domain.member.service.dto.request.MemberCreateCommand;

public interface MemberService {

    Long join(MemberCreateCommand command);
    MemberListResponse findAllMembers();
    void deleteMember(Long memberId);
    MemberDetailResponse getMemberDetail(Long memberId);
}
