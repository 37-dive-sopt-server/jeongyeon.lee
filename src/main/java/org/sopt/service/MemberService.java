package org.sopt.service;

import org.sopt.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    Long join(String name, int birthYear, int birthMonth, int birthDay, String email, String gender);
    Optional<Member> findOne(Long memberId);
    List<Member> findAllMembers();

}
