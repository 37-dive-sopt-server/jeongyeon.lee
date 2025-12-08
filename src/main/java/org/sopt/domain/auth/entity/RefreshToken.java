package org.sopt.domain.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RefreshToken {
    @Id
    private Long memberId;

    @Column(nullable = false, length = 500)
    private String refreshToken;

    public static RefreshToken of(Long memberId, String refreshToken) {
        return new RefreshToken(memberId, refreshToken);
    }

    public void updateToken(String token) {
        this.refreshToken = token;
    }
}
