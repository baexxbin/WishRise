package com.baexxbin.wishrise.auth.controller;

import com.baexxbin.wishrise.auth.dto.LoginResponse;
import com.baexxbin.wishrise.auth.jwt.TokenProvider;
import com.baexxbin.wishrise.auth.service.CustomOAuth2UserService;
import com.baexxbin.wishrise.auth.service.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final TokenService tokenService;
    private final TokenProvider tokenProvider;

//    @GetMapping("/auth/success")
//    public ResponseEntity<LoginResponse> loginSuccess(@Valid LoginResponse loginResponse) {
//        return ResponseEntity.ok(loginResponse);
//    }

    // 인증 성공 후 로그인 상태 확인
    @GetMapping("/success")
    public ResponseEntity<LoginResponse> loginSuccess(
            @CookieValue(value = "accessToken", required = false) String accessToken) {
        if (accessToken == null || !tokenProvider.validateToken(accessToken)) {
            // 토큰이 없거나 유효하지 않으면 로그인 실패 메시지와 함께 오류 응답 반환
            return ResponseEntity.status(401).body(
                    LoginResponse.builder()
                            .accessToken("Invalid or expired token")
                            .userId(null)      // 필요한 다른 필드는 null로 설정
                            .username(null)
                            .role(null)
                            .build()
            );
        }
        LoginResponse loginResponse = tokenService.getLoginResponse(accessToken);
        return ResponseEntity.ok(loginResponse);
    }


//    @DeleteMapping("/auth/logout")
//    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetails userDetails) {
//        tokenService.deleteRefreshToken(userDetails.getUsername());
//        return ResponseEntity.noContent().build();
//    }

    // 로그아웃 처리
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(value = "accessToken", required = false) String accessToken) {
        if (accessToken != null) {
            tokenService.deleteRefreshToken(accessToken);
        }
        return ResponseEntity.noContent().build();
    }

    // 리프레시 토큰을 이용해 accessToken 재발급
    @PostMapping("/reissue")
    public ResponseEntity<String> reissueAccessToken(
            @CookieValue(value = "accessToken", required = false) String accessToken,
            HttpServletResponse response) {
        if (accessToken == null || !tokenProvider.validateToken(accessToken)) {
            return ResponseEntity.status(401).body("Invalid or expired access token.");
        }

        // 리프레시 토큰을 이용해 새로운 accessToken 발급
        String newAccessToken = tokenService.reissueAccessToken(accessToken);

        if (newAccessToken == null) {
            return ResponseEntity.status(401).body("Reissue failed. Token invalid or expired.");
        }

        // 새 액세스 토큰 쿠키 추가
        Cookie cookie = new Cookie("accessToken", newAccessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(30 * 60); // 30분
        response.addCookie(cookie);

        return ResponseEntity.ok("Access token reissued successfully.");
    }


}
