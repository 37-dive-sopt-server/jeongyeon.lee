package org.sopt.domain.member.repository;

import org.sopt.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    Optional<Member> findById(Long id);

    List<Member> findAll();

    boolean existsByEmail(String email);

    void deleteById(Long id);
}
