package org.sopt.domain.member.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.member.constant.Gender;
import org.sopt.domain.member.fixture.MemberFixture;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @DisplayName("Member 객체를 생성할 수 있다.")
    @Test
    void createMember(){
       //given
        LocalDate birthDate = MemberFixture.MEMBER_DEFAULT_BIRTHDATE;

        //when
        Member member = Member.create(MemberFixture.MEMBER_NAME, MemberFixture.MEMBER_PASSWORD, birthDate,
                MemberFixture.MEMBER_EMAIL, Gender.MALE);

        //then
        assertThat(member.getName()).isEqualTo(MemberFixture.MEMBER_NAME);
        assertThat(member.getPassword()).isEqualTo(MemberFixture.MEMBER_PASSWORD);
    }

    @DisplayName("회원의 생년월일을 통해 회원의 나이를 계산할 수 있다.")
    @Test
    void getMemberAge(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        //when
        int age = member.getAge();
        //then
        assertThat(age).isEqualTo(26);
    }

}