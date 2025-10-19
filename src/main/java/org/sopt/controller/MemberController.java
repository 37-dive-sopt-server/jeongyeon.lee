package org.sopt.controller;

import org.sopt.domain.Member;
import org.sopt.service.MemberService;
import org.sopt.util.validator.EmailValidator;
import org.sopt.util.validator.GenderValidator;
import org.sopt.util.validator.MemberNameValidator;
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
