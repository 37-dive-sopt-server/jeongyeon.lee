package org.sopt.domain.auth.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.auth.dto.response.TokenResponse;
import org.sopt.domain.auth.entity.RefreshToken;
import org.sopt.domain.auth.errorcode.AuthErrorCode;
import org.sopt.domain.auth.repository.RefreshTokenRepository;
import org.sopt.domain.member.entity.Member;
import org.sopt.domain.member.errorcode.MemberErrorCode;
import org.sopt.domain.member.fixture.MemberFixture;
import org.sopt.domain.member.repository.MemberRepository;
import org.sopt.global.exception.customexception.CustomException;
import org.sopt.global.jwt.JwtProvider;
import org.sopt.global.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DisplayName("로그인에 성공한다.")
    @Test
    void login(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        member.updatePassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);

        //when
        TokenResponse response = authService.login(member.getEmail(), MemberFixture.MEMBER_PASSWORD);

        //then
        assertThat(response.memberId()).isEqualTo(member.getId());
        assertThat(response.accessToken()).isNotNull();
        assertThat(response.refreshToken()).isNotNull();

        Long memberId = jwtUtil.extractMemberIdFromToken(response.accessToken());
        assertThat(memberId).isEqualTo(member.getId());

        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(memberId).orElseThrow();
        assertThat(refreshToken.getRefreshToken()).isEqualTo(response.refreshToken());

    }

    @DisplayName("로그인 시 입력한 이메일이 존재하지 않으면 예외가 발생한다.")
    @Test
    void loginWithNotExistEmail(){

        assertThatThrownBy(() -> authService.login("test", MemberFixture.MEMBER_PASSWORD))
                .isInstanceOf(CustomException.class)
                .hasMessage(MemberErrorCode.MEMBER_NOT_FOUND.getMessage());

    }

    @DisplayName("로그인 시 비밀번호가 일치하지 않으면 예외가 발생한다.")
    @Test
    void loginWithMismatchPassword(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        member.updatePassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);

       //when
        assertThatThrownBy(() -> authService.login(member.getEmail(), "Invalid Password"))
                .isInstanceOf(CustomException.class)
                .hasMessage(MemberErrorCode.PASSWORD_MISMATCH.getMessage());

    }

    @DisplayName("로그인 시 Refresh Token이 이미 있다면 Refresh Token이 업데이트 된다.")
    @Test
    void loginWithExistingRefreshToken(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        member.updatePassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);

        refreshTokenRepository.save(
                RefreshToken.of(member.getId(), "test")
        );

       //when
        TokenResponse response = authService.login(member.getEmail(), MemberFixture.MEMBER_PASSWORD);
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(member.getId()).orElseThrow();

        //then
        assertThat(refreshToken.getRefreshToken()).isNotEqualTo("test");
    }

    @DisplayName("Refresh Token을 재발급 받는다.")
    @Test
    void reissue(){
       //given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);

        String oldRefreshToken = jwtProvider.generateRefreshToken(member.getId());
        refreshTokenRepository.save(RefreshToken.of(member.getId(), oldRefreshToken));

        //when
        TokenResponse response = authService.reissue(oldRefreshToken);

        //then
        assertThat(response.memberId()).isEqualTo(member.getId());
        assertThat(response.accessToken()).isNotNull();
        assertThat(response.refreshToken()).isNotNull();

    }

    @DisplayName("Refresh Token 재발급 시 유효하지 않은 토큰이 요청이 온 경우 예외가 발생한다.")
    @Test
    void reissueWithInvalidRefreshToken(){

        assertThatThrownBy(() -> authService.reissue("Bearer Invalid Token"))
                .isInstanceOf(CustomException.class);

    }

    @DisplayName("Refresh Token 재발급 시 Refresh Token이 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void reissueWithNotExistRefreshToken(){
       //given
        String oldRefreshToken = jwtProvider.generateRefreshToken(1L);

        //when && then
        assertThatThrownBy(() -> authService.reissue("Bearer " + oldRefreshToken))
                .isInstanceOf(CustomException.class)
                .hasMessage(AuthErrorCode.REFRESH_NOT_FOUND.getMessage());

    }

    @DisplayName("Refresh Token 재발급 시 저장된 토큰과 다르면 예외가 발생한다.")
    @Test
    void reisueWithMismatchRefreshToken(){
        // given
        Member member = MemberFixture.getmember(MemberFixture.MEMBER_DEFAULT_BIRTHDATE);
        memberRepository.save(member);

        String requestRefreshToken = jwtProvider.generateRefreshToken(member.getId());

        String storedRefreshToken = requestRefreshToken + "tampered";
        refreshTokenRepository.save(RefreshToken.of(member.getId(), storedRefreshToken));

        // when && then
        assertThatThrownBy(() -> authService.reissue("Bearer " + requestRefreshToken))
                .isInstanceOf(CustomException.class)
                .hasMessage(AuthErrorCode.REFRESH_MISMATCH.getMessage());
    }

}