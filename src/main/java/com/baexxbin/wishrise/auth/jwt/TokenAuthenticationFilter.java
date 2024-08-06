package com.baexxbin.wishrise.auth.jwt;

import com.baexxbin.wishrise.global.constants.TokenKey;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


/*
 * 토큰에 대한 인증 처리 진행
 * 토큰 검중 후 유효한 경우 인증정보 설정, 만료된 경우 새 토큰 발급받아 응답 헤더 포함
 * */

@RequiredArgsConstructor
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = resolveToken(request);

        // accessToken 검증
        if (tokenProvider.validateToken(accessToken)) {
            setAuthentication(accessToken);
        } else {
            // 만료되었을 경우 accessToken 재발급
            String reissueAccessToken = tokenProvider.reissueAccessToken(accessToken);

            if (StringUtils.hasText(reissueAccessToken)) {
                setAuthentication(reissueAccessToken);

                // 재발급된 accessToken 다시 전달
                response.setHeader(AUTHORIZATION, TokenKey.TOKEN_PREFIX + reissueAccessToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    // 인증 정보 설정
    private void setAuthentication(String accessToken) {
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // 요청에서 토큰 추출
    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);
        if (ObjectUtils.isEmpty(token) || !token.startsWith(TokenKey.TOKEN_PREFIX)) {
            return null;
        }
        return token.substring(TokenKey.TOKEN_PREFIX.length());
    }
}
