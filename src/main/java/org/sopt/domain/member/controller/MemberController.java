package org.sopt.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.member.dto.request.MemberCreateRequest;
import org.sopt.domain.member.dto.response.MemberDetailResponse;
import org.sopt.domain.member.dto.response.MemberListResponse;
import org.sopt.domain.member.service.MemberService;
import org.sopt.global.response.BaseResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public BaseResponse<Long> createMember(@Valid @RequestBody MemberCreateRequest request) {
        return BaseResponse.create(memberService.join(request.toCommand()),"회원 생성 완료");
    }

    @GetMapping("{memberId}")
    public BaseResponse<MemberDetailResponse> findMemberById(@PathVariable Long memberId) {
        return BaseResponse.ok(memberService.getMemberDetail(memberId), "회원 조회 완료");
    }

    @GetMapping()
    public BaseResponse<MemberListResponse> getAllMembers() {
        return BaseResponse.ok(memberService.findAllMembers(),"회원 목록 조회 완료");
    }

    @DeleteMapping("{memberId}")
    public BaseResponse<Void> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return BaseResponse.ok("회원 삭제 완료");
    }
}
