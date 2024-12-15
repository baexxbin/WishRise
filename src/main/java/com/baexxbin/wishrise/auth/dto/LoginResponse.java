package com.baexxbin.wishrise.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/*
* 로그인 성공 시 클라이언트로 반환되는 데이터 구조
* - 인증 성공 후 클라이언트에 필요한 정보 전달 (주로 jwt토큰, 사용자 정보 반환)
* - 클라이언트가 이후 요청에서 토큰을 사용해 인증을 유지하도록 지원
* */

public record LoginResponse(String accessToken,
                            String refreshToken,
                            String userId,
                            String username,
                            String role) {
    @Builder
    public LoginResponse {
    }
}
