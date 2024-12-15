package com.baexxbin.wishrise.auth.handler;

import com.baexxbin.wishrise.auth.jwt.TokenProvider;
import com.baexxbin.wishrise.auth.service.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/*
* OAuth2 인증 과정에서 성공적인 인증 이후의 동작 정의
* */

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final TokenService tokenService;
    private static final String URI = "/auth/success";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 1. Access Token 생성
        String accessToken = tokenProvider.generateAccessToken(authentication);

        // 2. Refresh Token 생성
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        // 3. Token 저장
        String memberKey = authentication.getName();
        tokenService.saveOrUpdate(memberKey, refreshToken, accessToken);

        // 4. Access Token을 쿠키에 저장
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true); // JS에서 접근 불가
        accessTokenCookie.setSecure(true);   // HTTPS에서만 전송
        accessTokenCookie.setPath("/");      // 모든 경로에서 쿠키 전송
        accessTokenCookie.setMaxAge(30 * 60); // 30분간 유효

        // 5. Refresh Token을 쿠키에 저장
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true); // JS에서 접근 불가
        refreshTokenCookie.setPath("/");      // 모든 경로에서 쿠키 전송
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7일간 유효

        // 6. 응답에 쿠키 추가
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        // 7. JSON 형태로 응답을 보내고 리다이렉트
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"Login successful\", \"redirectUrl\": \"/auth/success\"}");
        response.setStatus(HttpServletResponse.SC_OK);

    }

}
