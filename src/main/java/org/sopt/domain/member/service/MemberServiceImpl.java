package org.sopt.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.member.constant.Gender;
import org.sopt.domain.member.constant.MemberConstant;
import org.sopt.domain.member.dto.response.MemberDetailResponse;
import org.sopt.domain.member.dto.response.MemberListResponse;
import org.sopt.domain.member.entity.Member;
import org.sopt.domain.member.repository.MemberRepository;
import org.sopt.domain.member.service.dto.request.MemberCreateCommand;
import org.sopt.global.config.cache.CacheNameConstant;
import org.sopt.global.exception.customexception.CustomException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.sopt.domain.member.errorcode.MemberErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @CacheEvict(cacheNames = CacheNameConstant.MEMBER_LIST, allEntries = true)
    public Long join(MemberCreateCommand command) {
        checkEmailDuplicate(command.email());

        Member member = Member.create(
                command.name(),
                passwordEncoder.encode(command.password()),
                LocalDate.parse(command.birthDate()),
                command.email(),
                Gender.valueOf(command.gender()));

        validateMemberAge(member);

        memberRepository.save(member);
        return member.getId();
    }

    private void validateMemberAge(Member member) {
        int age = member.getAge();
        if(age < MemberConstant.MEMBER_MINIMUM_AGE) {
            throw new CustomException(MEMBER_AGE_TOO_LOW);
        }
    }

    private void checkEmailDuplicate(String email) {
        if(memberRepository.existsByEmail(email)) {
            throw new CustomException(DUPLICATE_EMAIL);
        }
    }

    @Cacheable(cacheNames = CacheNameConstant.MEMBER_LIST)
    public MemberListResponse findAllMembers() {
        List<MemberDetailResponse> memberDetails = memberRepository.findAll().stream()
                .map(MemberDetailResponse::from)
                .toList();
        return new MemberListResponse(memberDetails);
    }

    @Transactional
    @CacheEvict(cacheNames = CacheNameConstant.MEMBER_DETAIL, key = "#memberId")
    public void deleteMember(Long memberId) {
        Member member = findById(memberId);

        memberRepository.deleteById(memberId);
    }

    @Cacheable(cacheNames = CacheNameConstant.MEMBER_DETAIL, key = "#memberId")
    public MemberDetailResponse getMemberDetail(Long memberId) {
        Member member = findById(memberId);
        return MemberDetailResponse.from(member);
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
    }
}
