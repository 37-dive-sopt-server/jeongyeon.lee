package org.sopt.service;

import org.sopt.domain.Member;

import java.util.List;

public interface MemberService {

    Long join(String name, int birthYear, int birthMonth, int birthDay, String email, String gender);
    List<Member> findAllMembers();
    void deleteMember(Long memberId);
    Member findById(Long memberId);
}
