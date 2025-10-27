package org.sopt.domain.member.controller;

import org.sopt.domain.member.dto.request.MemberCreateRequest;
import org.sopt.domain.member.dto.response.MemberDetailResponse;
import org.sopt.domain.member.service.MemberService;
import org.sopt.global.response.BaseResponse;
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
    public BaseResponse<Long> createMember(MemberCreateRequest request) {

        MemberNameValidator.validateName(request.name());

        EmailValidator.validateEmail(request.email());

        GenderValidator.validateGender(request.gender());

        return BaseResponse.create(memberService.join(request.name(), request.birthDate(), request.email(), request.gender()),"회원 생성 완료");
    }

    @GetMapping("{memberId}")
    public BaseResponse<MemberDetailResponse> findMemberById(@PathVariable Long memberId) {
        return BaseResponse.ok(memberService.getMemberDetail(memberId), "회원 조회 완료");
    }

    @GetMapping("all")
    public BaseResponse<List<MemberDetailResponse>> getAllMembers() {
        return BaseResponse.ok(memberService.findAllMembers(),"회원 목록 조회 완료");
    }

    @DeleteMapping("{memberId}")
    public BaseResponse<Void> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return BaseResponse.ok("회원 삭제 완료");
    }
}
