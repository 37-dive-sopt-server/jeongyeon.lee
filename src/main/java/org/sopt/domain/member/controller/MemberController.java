package org.sopt.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.domain.member.dto.request.MemberCreateRequest;
import org.sopt.domain.member.dto.response.MemberDetailResponse;
import org.sopt.domain.member.dto.response.MemberListResponse;
import org.sopt.domain.member.service.MemberService;
import org.sopt.global.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<BaseResponse<Long>> createMember(@Valid @RequestBody MemberCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.create(memberService.join(request.toCommand()),"회원 생성 완료"));
    }

    @GetMapping("{memberId}")
    public ResponseEntity<BaseResponse<MemberDetailResponse>> findMemberById(@PathVariable Long memberId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.ok(memberService.getMemberDetail(memberId), "회원 조회 완료"));
    }

    @GetMapping()
    public ResponseEntity<BaseResponse<MemberListResponse>> getAllMembers() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.ok(memberService.findAllMembers(),"회원 목록 조회 완료"));
    }

    @DeleteMapping("{memberId}")
    public ResponseEntity<BaseResponse<Void>> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.ok("회원 삭제 완료"));
    }
}
