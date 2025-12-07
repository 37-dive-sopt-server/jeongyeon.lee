package org.sopt.domain.auth.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sopt.domain.auth.entity.RefreshToken;
import org.sopt.global.config.QuerydslConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslConfig.class)
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("MemberId로 RefreshToken을 찾을 수 있다.")
    void findByMemberId() {
        // given
        RefreshToken token = RefreshToken.of(1L, "test");

        // when
        refreshTokenRepository.save(token);

        // then
        RefreshToken found = refreshTokenRepository.findByMemberId(1L).orElseThrow();
        assertThat(found.getRefreshToken()).isEqualTo("test");
    }

    @Test
    @DisplayName("같은 memberId로 저장하면 업데이트된다")
    void updateTokenWhenDuplicateMemberId() {
        // given
        refreshTokenRepository.save(RefreshToken.of(1L, "old token"));

        RefreshToken updated = RefreshToken.of(1L, "new token");

        // when
        refreshTokenRepository.save(updated);

        // then
        RefreshToken found = refreshTokenRepository.findById(1L).orElseThrow();
        assertThat(found.getRefreshToken()).isEqualTo("new token");
    }

}