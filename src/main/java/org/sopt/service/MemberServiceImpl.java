package org.sopt.service;

import org.sopt.domain.Gender;
import org.sopt.domain.Member;
import org.sopt.repository.MemoryMemberRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.sopt.constant.ErrorMessage.*;

public class MemberServiceImpl implements MemberService {

    private final MemoryMemberRepository memberRepository =  new MemoryMemberRepository();
    private static long sequence = 1L;

    public Long join(String name, int birthYear, int birthMonth, int birthDay, String email, String gender) {
        checkEmailDuplicate(email);

        Member member = Member.create(sequence++,
                name,
                LocalDate.of(birthYear, birthMonth, birthDay),
                email,
                Gender.valueOf(gender));

        validateMemberAge(member);

        memberRepository.save(member);
        return member.getId();
    }

    private void validateMemberAge(Member member) {
        int age = member.getAge();
        if(age < 20){
            throw new IllegalArgumentException(MEMBER_AGE_TOO_LOW.getMessage());
        }
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

    public void deleteMember(Long memberId) {
        Member member = findById(memberId);

        memberRepository.deleteById(memberId);
    }

    private Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException(MEMBER_NOT_FOUND.getMessage()));
    }
}
