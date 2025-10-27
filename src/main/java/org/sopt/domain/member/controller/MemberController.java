package org.sopt.domain.member.controller;

import org.sopt.domain.member.dto.request.MemberCreateRequest;
import org.sopt.domain.member.dto.response.MemberDetailResponse;
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
    public Long createMember(MemberCreateRequest request) {

        MemberNameValidator.validateName(request.name());

        EmailValidator.validateEmail(request.email());

        GenderValidator.validateGender(request.gender());

        return memberService.join(request.name(), request.birthDate(), request.email(), request.gender());
    }

    @GetMapping()
    public MemberDetailResponse findMemberById(Long id) {
        return memberService.getMemberDetail(id);
    }

    @GetMapping("all")
    public List<MemberDetailResponse> getAllMembers() {
        return memberService.findAllMembers();
    }

    @DeleteMapping
    public void deleteMember(Long memberId) {
        memberService.deleteMember(memberId);
    }
}
