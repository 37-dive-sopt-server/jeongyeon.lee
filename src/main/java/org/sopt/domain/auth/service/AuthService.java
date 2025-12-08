package org.sopt.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.sopt.domain.auth.dto.response.TokenResponse;
import org.sopt.domain.auth.entity.RefreshToken;
import org.sopt.domain.auth.repository.RefreshTokenRepository;
import org.sopt.domain.member.entity.Member;
import org.sopt.domain.member.repository.MemberRepository;
import org.sopt.global.exception.customexception.CustomException;
import org.sopt.global.jwt.JwtProvider;
import org.sopt.global.jwt.JwtUtil;
import org.sopt.global.jwt.constant.HttpHeaderConstants;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.sopt.domain.auth.errorcode.AuthErrorCode.*;
import static org.sopt.domain.member.errorcode.MemberErrorCode.MEMBER_NOT_FOUND;
import static org.sopt.domain.member.errorcode.MemberErrorCode.PASSWORD_MISMATCH;
import static org.sopt.global.exception.errorcode.GlobalErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final JwtProvider jwtProvider;

    private final MemberRepository memberRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public TokenResponse login(String email, String password) {

        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        checkPassword(password, member);

        String accessToken = jwtProvider.generateAccessToken(member.getId());
        String refreshToken = jwtProvider.generateRefreshToken(member.getId());

        refreshTokenRepository.findByMemberId(member.getId())
                .ifPresentOrElse(
                        existing -> existing.updateToken(refreshToken),
                        () -> refreshTokenRepository.save(RefreshToken.of(member.getId(), refreshToken))
                );

        return TokenResponse.of(member.getId(), accessToken, refreshToken);

    }

    private void checkPassword(String password, Member member) {
        if(!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(PASSWORD_MISMATCH);
        }
    }

    public TokenResponse reissue(String bearerToken) {
        String refreshToken = bearerToken.replace(HttpHeaderConstants.BEARER_PREFIX, "").trim();
        if(!jwtUtil.isTokenValid(refreshToken)) {
            throw new CustomException(JWT_INVALID);
        }

        Long memberId = jwtUtil.extractMemberIdFromToken(refreshToken);

        RefreshToken savedToken = refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomException(REFRESH_NOT_FOUND));

        if (!savedToken.getRefreshToken().equals(refreshToken)) {
            throw new CustomException(REFRESH_MISMATCH);
        }

        String newAccessToken = jwtProvider.generateAccessToken(memberId);
        String newRefreshToken = jwtProvider.generateRefreshToken(memberId);

        savedToken.updateToken(newRefreshToken);

        return TokenResponse.of(memberId, newAccessToken, newRefreshToken);
    }


}
