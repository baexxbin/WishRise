package com.baexxbin.wishrise.auth.jwt;

import com.baexxbin.wishrise.auth.service.TokenService;
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
    private final TokenService tokenService;

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String accessToken = resolveToken(request);
//
//        // accessToken 검증
//        if (tokenProvider.validateToken(accessToken)) {
//            setAuthentication(accessToken);
//        } else {
//            // 만료되었을 경우 accessToken 재발급
//            String reissueAccessToken = tokenProvider.reissueAccessToken(accessToken);
//
//            if (StringUtils.hasText(reissueAccessToken)) {
//                setAuthentication(reissueAccessToken);
//
//                // 재발급된 accessToken 다시 전달
//                response.setHeader(AUTHORIZATION, TokenKey.TOKEN_PREFIX + reissueAccessToken);
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = resolveToken(request);

        // accessToken 검증
        if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
            setAuthentication(accessToken);
        } else {
            // 만료되었을 경우, 리프레시 토큰을 통해 새 accessToken 발급
            String refreshToken = resolveRefreshToken(request);
            if (StringUtils.hasText(refreshToken) && tokenProvider.validateToken(refreshToken)) {
                // 리프레시 토큰이 유효하면 accessToken 재발급
                String newAccessToken = tokenService.reissueAccessToken(refreshToken);

                if (StringUtils.hasText(newAccessToken)) {
                    setAuthentication(newAccessToken);

                    // 재발급된 accessToken을 응답 헤더에 포함
                    response.setHeader(AUTHORIZATION, TokenKey.TOKEN_PREFIX + newAccessToken);
                }
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

    // 요청에서 리프레시 토큰 추출
    private String resolveRefreshToken(HttpServletRequest request) {
        // 리프레시 토큰은 따로 헤더에 담아서 보내는 방식이라면 이곳에서 추출
        // 예를 들어, Authorization 헤더와 다른 방식으로 전달된다면 이 부분을 수정
        String token = request.getHeader("X-Refresh-Token"); // 예시로 X-Refresh-Token 헤더 사용
        if (ObjectUtils.isEmpty(token) || !token.startsWith(TokenKey.TOKEN_PREFIX)) {
            return null;
        }
        return token.substring(TokenKey.TOKEN_PREFIX.length());
    }
}
