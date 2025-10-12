package org.sopt.service;

import org.sopt.domain.Gender;
import org.sopt.domain.Member;
import org.sopt.repository.MemoryMemberRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.sopt.constant.ErrorMessage.DUPLICATE_EMAIL;

public class MemberServiceImpl implements MemberService {

    private final MemoryMemberRepository memberRepository =  new MemoryMemberRepository();
    private static long sequence = 1L;

    public Long join(String name, int birthYear, int birthMonth, int birthDay, String email, String gender) {
        checkEmailDuplicate(email);
        
        Member member = new Member(sequence++, name,
                LocalDate.of(birthYear, birthMonth, birthDay),
                email,
                Gender.valueOf(gender));
        memberRepository.save(member);
        return member.getId();
    }

    private void checkEmailDuplicate(String email) {
        if(memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(DUPLICATE_EMAIL.getMessage());
        }
    }

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }
}
