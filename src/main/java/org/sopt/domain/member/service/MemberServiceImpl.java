package org.sopt.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.member.constant.Gender;
import org.sopt.domain.member.dto.response.MemberDetailResponse;
import org.sopt.domain.member.dto.response.MemberListResponse;
import org.sopt.domain.member.entity.Member;
import org.sopt.domain.member.repository.MemberRepository;
import org.sopt.domain.member.service.dto.request.MemberCreateCommand;
import org.sopt.global.exception.customexception.CustomException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static org.sopt.domain.member.errorcode.MemberErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private static final int MINIMUM_MEMBER_AGE = 20;

    public Long join(MemberCreateCommand command) {
        checkEmailDuplicate(command.email());

        Member member = Member.create(
                command.name(),
                LocalDate.parse(command.birthDate()),
                command.email(),
                Gender.valueOf(command.gender()));

        validateMemberAge(member);

        memberRepository.save(member);
        return member.getId();
    }

    private void validateMemberAge(Member member) {
        int age = member.getAge();
        if(age < MINIMUM_MEMBER_AGE){
            throw new CustomException(MEMBER_AGE_TOO_LOW);
        }
    }

    private void checkEmailDuplicate(String email) {
        if(memberRepository.existsByEmail(email)) {
            throw new CustomException(DUPLICATE_EMAIL);
        }
    }

    public MemberListResponse findAllMembers() {
        List<MemberDetailResponse> memberDetails = memberRepository.findAll().stream()
                .map(MemberDetailResponse::from)
                .toList();
        return new MemberListResponse(memberDetails);
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
        return memberRepository.findById(memberId).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
    }
}
