package org.sopt.domain.member.service;

import org.sopt.domain.member.entity.Member;

import java.util.List;

public interface MemberService {

    Long join(String name, String birthDate, String email, String gender);
    List<Member> findAllMembers();
    void deleteMember(Long memberId);
    Member findById(Long memberId);
}
