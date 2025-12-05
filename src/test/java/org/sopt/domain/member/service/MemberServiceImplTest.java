package org.sopt.domain.member.service;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.member.constant.Gender;
import org.sopt.domain.member.dto.response.MemberDetailResponse;
import org.sopt.domain.member.dto.response.MemberListResponse;
import org.sopt.domain.member.entity.Member;
import org.sopt.domain.member.fixture.MemberFixture;
import org.sopt.domain.member.repository.MemberRepository;
import org.sopt.domain.member.service.dto.request.MemberCreateCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.sopt.domain.member.errorcode.MemberErrorCode.*;

@SpringBootTest
@ActiveProfiles("test")
class MemberServiceImplTest {

    @Autowired
    private MemberServiceImpl memberServiceImpl;

    @SpyBean
    private MemberRepository memberRepository;

    @Autowired
    private CacheManager cacheManager;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
        cacheManager.getCacheNames().forEach(cacheName -> {
            Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
        });
    }

    @DisplayName("")
    @Test
    void join(){
       //given
        MemberCreateCommand command = MemberCreateCommand.builder()
                .email("test1@test.com")
                .name(MemberFixture.MEMBER_NAME)
                .password(MemberFixture.MEMBER_PASSWORD)
                .birthDate(MemberFixture.MEMBER_DEFAULT_BIRTHDATE.toString())
                .gender(Gender.MALE.toString())
                .build();

       //when
        Long memberId = memberServiceImpl.join(command);

        Member member = memberRepository.findById(memberId).orElseThrow();

        //then
        assertThat(memberId).isEqualTo(member.getId());
    }

    @DisplayName("회원가입 시 이메일이 중복되면 예외가 발생한다.")
    @Test
    void joinWithDuplicateEmail(){
       //given
        Member member1 = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member1);

        MemberCreateCommand command = MemberCreateCommand.builder()
                .email(MemberFixture.MEMBER_EMAIL)
                .name(MemberFixture.MEMBER_NAME)
                .password(MemberFixture.MEMBER_PASSWORD)
                .birthDate(MemberFixture.MEMBER_DEFAULT_BIRTHDATE.toString())
                .gender(Gender.MALE.toString())
                .build();
        //when && then
        assertThatThrownBy(() -> memberServiceImpl.join(command)).hasMessage(DUPLICATE_EMAIL.getMessage());

    }

    @DisplayName("20세 미만 회원이 회원 가입 시 예외가 발생한다.")
    @Test
    void joinWithLowAge(){
       //given
        MemberCreateCommand command = MemberCreateCommand.builder()
                .email(MemberFixture.MEMBER_EMAIL)
                .name(MemberFixture.MEMBER_NAME)
                .password(MemberFixture.MEMBER_PASSWORD)
                .birthDate(MemberFixture.MEMBER_YOUNG_BIRTHDATE.toString())
                .gender(Gender.MALE.toString())
                .build();
       //when && then
        assertThatThrownBy(() -> memberServiceImpl.join(command)).hasMessage(MEMBER_AGE_TOO_LOW.getMessage());

    }

    @DisplayName("회원의 ID로 회원을 삭제할 수 있다.")
    @Test
    void deleteMember(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);

        //when
        memberServiceImpl.deleteMember(member.getId());

       //then
        assertThat(memberRepository.findById(member.getId())).isEmpty();
    }

    @DisplayName("존재하지 않는 회원의 ID로 회원을 삭제하려고 하면 예외가 발생한다.")
    @Test
    void delteteMemberWithoutId(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);

       //when && then
        assertThatThrownBy(() -> memberServiceImpl.deleteMember(2L)).hasMessage(MEMBER_NOT_FOUND.getMessage());

    }

    @DisplayName("회원의 상세 정보를 조회할 수 있다.")
    @Test
    void getMemberDetail(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);

       //when
        MemberDetailResponse memberDetail = memberServiceImpl.getMemberDetail(member.getId());

        //then
        assertThat(memberDetail).isNotNull()
                .extracting("name","email","birthDate")
                .contains(
                        MemberFixture.MEMBER_NAME, MemberFixture.MEMBER_EMAIL, MemberFixture.MEMBER_DEFAULT_BIRTHDATE
                );
    }

    @DisplayName("회원 목록을 조회할 수 있다")
    @Test
    void findAllMembers(){

       //given
        createMembers();

        //when
        MemberListResponse memberListResponse = memberServiceImpl.findAllMembers();

        //then
        assertThat(memberListResponse.members()).hasSize(3)
                .extracting("email","name")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(MemberFixture.MEMBER_EMAIL, MemberFixture.MEMBER_NAME),
                        Tuple.tuple(MemberFixture.MEMBER_EMAIL, MemberFixture.MEMBER_NAME),
                        Tuple.tuple(MemberFixture.MEMBER_EMAIL, MemberFixture.MEMBER_NAME)
                );
    }


    @DisplayName("회원 목록 조회 시 캐싱이 발생하여 쿼리는 1번만 발생한다.")
    @Test
    void findAllMembersWithCaching(){
       //given
        createMembers();

       //when
        IntStream.range(0, 10)
                .forEach(i -> memberServiceImpl.findAllMembers());

       //then
        verify(memberRepository, times(1)).findAll();

    }

    @DisplayName("회원 목록 조회 캐싱 후 새로운 회원이 추가되면 기존 캐시는 삭제되고 새로운 쿼리가 실행된다.")
    @Test
    void findAllMemberWithCacheEvict(){
       //given
        createMembers();

       //when
        IntStream.range(0, 10)
                .forEach(i -> memberServiceImpl.findAllMembers());

        MemberCreateCommand command = MemberCreateCommand.builder()
                .email("test1@test.com")
                .name(MemberFixture.MEMBER_NAME)
                .password(MemberFixture.MEMBER_PASSWORD)
                .birthDate(MemberFixture.MEMBER_DEFAULT_BIRTHDATE.toString())
                .gender(Gender.MALE.toString())
                .build();

        memberServiceImpl.join(command);
        IntStream.range(0, 10)
                .forEach(i -> memberServiceImpl.findAllMembers());

       //then
        verify(memberRepository, times(2)).findAll();
    }



    private void createMembers() {
        Member member1 = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        Member member2 = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        Member member3 = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.saveAll(List.of(member1, member2, member3));
    }


}