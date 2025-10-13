package org.sopt.controller;

import org.sopt.domain.Member;
import org.sopt.service.MemberService;
import org.sopt.util.validator.EmailValidator;
import org.sopt.util.validator.GenderValidator;
import org.sopt.util.validator.MemberNameValidator;

import java.util.List;
import java.util.Optional;

public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    public Long createMember(String name, int birthYear, int birthMonth, int birthDay, String email, String gender) {

        MemberNameValidator.validateName(name);

        EmailValidator.validateEmail(email);

        GenderValidator.validateGender(gender);

        return memberService.join(name, birthYear, birthMonth, birthDay, email, gender);
    }

    public Optional<Member> findMemberById(Long id) {
        return memberService.findOne(id);
    }

    public List<Member> getAllMembers() {
        return memberService.findAllMembers();
    }

    public void deleteMember(Long memberId) {
        memberService.deleteMember(memberId);
    }
}
