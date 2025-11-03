package org.sopt.domain.member.service;

import org.sopt.domain.member.dto.response.MemberDetailResponse;
import org.sopt.domain.member.dto.response.MemberListResponse;

public interface MemberService {

    Long join(String name, String birthDate, String email, String gender);
    MemberListResponse findAllMembers();
    void deleteMember(Long memberId);
    MemberDetailResponse getMemberDetail(Long memberId);
}
