package org.sopt.service;

import org.sopt.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    Long join(String name);
    Optional<Member> findOne(Long memberId);
    List<Member> findAllMembers();

}
