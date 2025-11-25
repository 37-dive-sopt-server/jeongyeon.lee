package org.sopt.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record TokenResponse(
        @Schema(description = "멤버 ID", example = "1")
        Long memberId,

        @Schema(description = "액세스 토큰")
        String accessToken,

        @Schema(description = "리프레시 토큰")
        String refreshToken
) {
   public static TokenResponse of(Long memberId, String accessToken, String refreshToken) {
           return new TokenResponse(memberId, accessToken, refreshToken);
   }
}
