package org.sopt.domain.member.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.member.entity.Member;
import org.sopt.domain.member.fixture.MemberFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("이메일로 회원의 존재 여부를 판단한다.")
    @Test
    void existByEmail(){
       //given
        Member member = memberRepository.save(MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE));

        //when
        boolean result = memberRepository.existsByEmail(member.getEmail());

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("회원의 ID를 통해 회원을 삭제할 수 있다.")
    @Test
    void deleteById(){
       //given
        Member member = memberRepository.save(MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE));

       //when
        memberRepository.deleteById(member.getId());

       //then
        boolean result = memberRepository.existsById(member.getId());
        assertThat(result).isFalse();
    }

    @DisplayName("회원의 이메일로 Member 객체를 찾을 수 있다.")
    @Test
    void findByEmail(){
       //given
        Member member = memberRepository.save(MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE));
        //when
        Optional<Member> foundMember = memberRepository.findByEmail(MemberFixture.MEMBER_EMAIL);

        //then
        assertThat(foundMember).isPresent();
    }

}