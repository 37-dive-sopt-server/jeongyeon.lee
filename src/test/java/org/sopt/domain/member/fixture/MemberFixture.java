package org.sopt.domain.member.fixture;

import org.sopt.domain.member.constant.Gender;
import org.sopt.domain.member.entity.Member;

import java.time.LocalDate;

public class MemberFixture {

    public static final String MEMBER_EMAIL = "test@test.com";
    public static final String MEMBER_PASSWORD = "test";
    public static final String MEMBER_NAME = "test";

    public static final LocalDate MEMBER_DEFAULT_BIRTHDATE = LocalDate.of(2000,11,12);
    public static final LocalDate MEMBER_YOUNG_BIRTHDATE = LocalDate.of(2010,11,12);


    public static Member getmember(String email, LocalDate birthDate){
        return Member.builder()
                .password(MEMBER_PASSWORD)
                .name(MEMBER_NAME)
                .birthDate(birthDate)
                .email(email)
                .gender(Gender.MALE)
                .build();
    }

    public static Member getmember(LocalDate birthDate){
        return Member.builder()
                .password(MEMBER_PASSWORD)
                .name(MEMBER_NAME)
                .birthDate(birthDate)
                .email(MEMBER_EMAIL)
                .gender(Gender.MALE)
                .build();
    }

}
