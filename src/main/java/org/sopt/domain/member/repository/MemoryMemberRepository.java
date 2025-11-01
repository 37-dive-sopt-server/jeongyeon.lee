package org.sopt.domain.member.repository;

import org.sopt.domain.member.entity.Member;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;

@Primary
@Repository
public class MemoryMemberRepository implements MemberRepository {

    private static final Map<Long, Member> store = new HashMap<>();

    public Member save(Member member) {

        store.put(member.getId(), member);
        return member;

    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }


    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public boolean existsByEmail(String email) {
        return store.values().stream().anyMatch(member -> member.getEmail().equals(email));
    }

    public void deleteById(Long id) {
        store.remove(id);
    }

}
