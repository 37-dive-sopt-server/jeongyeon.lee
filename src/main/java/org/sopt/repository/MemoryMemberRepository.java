package org.sopt.repository;

import org.sopt.domain.Member;

import java.util.*;

public class MemoryMemberRepository {

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
