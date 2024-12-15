package com.baexxbin.wishrise.auth.service;

import com.baexxbin.wishrise.auth.domain.Token;
import com.baexxbin.wishrise.auth.dto.LoginResponse;
import com.baexxbin.wishrise.auth.exception.TokenException;
import com.baexxbin.wishrise.auth.jwt.TokenProvider;
import com.baexxbin.wishrise.auth.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.baexxbin.wishrise.global.exception.ErrorCode.TOKEN_EXPIRED;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenService {
    private final TokenRepository tokenRepository;
    private final TokenProvider tokenProvider;


    public void deleteRefreshToken(String memberKey) {
        tokenRepository.deleteById(memberKey);
    }

    @Transactional
    public void saveOrUpdate(String memberKey, String refreshToken, String accessToken) {
        Token token = tokenRepository.findByAccessToken(accessToken)
                .map(o -> o.updateRefreshToken(refreshToken))
                .orElseGet(() -> new Token(memberKey, refreshToken, accessToken));

        tokenRepository.save(token);
    }

    public Token findByAccessTokenOrThrow(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new TokenException(TOKEN_EXPIRED));
    }

    @Transactional
    public void updateToken(String accessToken, Token token) {
        token.updateAccessToken(accessToken);
        tokenRepository.save(token);
    }

    public LoginResponse getLoginResponse(String accessToken) {
        if (!tokenProvider.validateToken(accessToken)) {
            throw new TokenException(TOKEN_EXPIRED);
        }

        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        String userId = authentication.getName();
        String username = authentication.getPrincipal().toString();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse("USER");

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken("refresh-token-placeholder") // 실제 리프레시 토큰 로직 추가
                .userId(userId)
                .username(username)
                .role(role)
                .build();
    }


    public String reissueAccessToken(String accessToken) {
        if (!tokenProvider.validateToken(accessToken)) {
            throw new TokenException(TOKEN_EXPIRED);
        }

        Token token = findByAccessTokenOrThrow(accessToken);
        String refreshToken = token.getRefreshToken();

        if (!tokenProvider.validateToken(refreshToken)) {
            throw new TokenException(TOKEN_EXPIRED);
        }

        String newAccessToken = tokenProvider.generateAccessToken(tokenProvider.getAuthentication(refreshToken));
        updateToken(newAccessToken, token);

        return newAccessToken;
    }
}
