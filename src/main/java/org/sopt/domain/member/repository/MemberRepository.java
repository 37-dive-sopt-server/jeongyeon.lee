package org.sopt.domain.member.repository;

import org.sopt.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Member save(Member member);

    Optional<Member> findById(Long id);

    List<Member> findAll();

    boolean existsByEmail(String email);

    void deleteById(Long id);
}
