package org.sopt.domain.member.service;

import org.sopt.domain.member.dto.response.MemberDetailResponse;

import java.util.List;

public interface MemberService {

    Long join(String name, String birthDate, String email, String gender);
    List<MemberDetailResponse> findAllMembers();
    void deleteMember(Long memberId);
    MemberDetailResponse getMemberDetail(Long memberId);
}
