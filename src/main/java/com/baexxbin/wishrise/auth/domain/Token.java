package com.baexxbin.wishrise.auth.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@RedisHash(value = "jwt", timeToLive = 60 * 60 * 24 * 7)
public class Token {
    @Id
    private String authId;      // Redis 해시 키

    @Indexed                    // 리프레시 토큰 조회 성능 향상
    private String refreshToken;

    private String accessToken;

    private String role;

    public Token(String memberKey, String refreshToken, String accessToken) {
    }


    public Token updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
