package org.sopt.domain.member.controller;

import org.sopt.domain.member.entity.Member;
import org.sopt.domain.member.service.MemberService;
import org.sopt.global.validator.EmailValidator;
import org.sopt.global.validator.GenderValidator;
import org.sopt.global.validator.MemberNameValidator;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public Long createMember(String name, int birthYear, int birthMonth, int birthDay, String email, String gender) {

        MemberNameValidator.validateName(name);

        EmailValidator.validateEmail(email);

        GenderValidator.validateGender(gender);

        return memberService.join(name, birthYear, birthMonth, birthDay, email, gender);
    }

    @GetMapping()
    public Member findMemberById(Long id) {
        return memberService.findById(id);
    }

    @GetMapping("all")
    public List<Member> getAllMembers() {
        return memberService.findAllMembers();
    }

    @DeleteMapping
    public void deleteMember(Long memberId) {
        memberService.deleteMember(memberId);
    }
}
