package org.sopt.controller;

import org.sopt.domain.Member;
import org.sopt.service.MemberService;
import org.sopt.service.MemberServiceImpl;

import java.util.List;
import java.util.Optional;

public class MemberController {

    private final MemberService memberService = new MemberServiceImpl();

    public Long createMember(String name, int  birthYear, int birthMonth, int birthDay, String email, String gender) {

        return memberService.join(name, birthYear, birthMonth, birthDay, email, gender);
    }

    public Optional<Member> findMemberById(Long id) {
        return memberService.findOne(id);
    }

    public List<Member> getAllMembers() {
        return memberService.findAllMembers();
    }
}
