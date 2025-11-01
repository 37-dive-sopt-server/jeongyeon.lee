package org.sopt.domain.member.service;

import org.sopt.domain.member.constant.Gender;
import org.sopt.domain.member.dto.response.MemberDetailResponse;
import org.sopt.domain.member.entity.Member;
import org.sopt.domain.member.repository.MemberRepository;
import org.sopt.domain.member.util.IdGenerator;
import org.sopt.global.exception.customexception.BadRequestException;
import org.sopt.global.exception.customexception.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static org.sopt.global.exception.constant.MemberErrorCode.*;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private static final int MINIMUM_MEMBER_AGE = 20;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long join(String name, String birthDate, String email, String gender) {
        checkEmailDuplicate(email);

        long memberId = IdGenerator.nextId();

        Member member = Member.create(memberId,
                name,
                LocalDate.parse(birthDate),
                email,
                Gender.valueOf(gender));

        validateMemberAge(member);

        memberRepository.save(member);
        return member.getId();
    }

    private void validateMemberAge(Member member) {
        int age = member.getAge();
        if(age < MINIMUM_MEMBER_AGE){
            throw new BadRequestException(MEMBER_AGE_TOO_LOW);
        }
    }

    private void checkEmailDuplicate(String email) {
        if(memberRepository.existsByEmail(email)) {
            throw new BadRequestException(DUPLICATE_EMAIL);
        }
    }

    public List<MemberDetailResponse> findAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberDetailResponse::from)
                .toList();
    }

    public void deleteMember(Long memberId) {
        Member member = findById(memberId);

        memberRepository.deleteById(memberId);
    }

    public MemberDetailResponse getMemberDetail(Long memberId) {
        Member member = findById(memberId);
        return MemberDetailResponse.from(member);
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));
    }
}
