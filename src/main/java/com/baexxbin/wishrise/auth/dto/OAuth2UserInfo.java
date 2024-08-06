package com.baexxbin.wishrise.auth.dto;

import com.baexxbin.wishrise.auth.exception.AuthException;
import com.baexxbin.wishrise.global.util.KeyGenerator;
import com.baexxbin.wishrise.member.domain.Member;
import com.baexxbin.wishrise.member.domain.Role;
import lombok.Builder;

import java.util.Map;

import static com.baexxbin.wishrise.global.exception.ErrorCode.ILLEGAL_REGISTRATION_ID;

@Builder
public record OAuth2UserInfo(
        String name,
        String email,
        String provider
) {
    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) { // registration id별로 userInfo 생성
            case "google" -> ofGoogle(attributes, registrationId);
            case "kakao" -> ofKakao(attributes, registrationId);
            default -> throw new AuthException(ILLEGAL_REGISTRATION_ID);
        };
    }

    private static OAuth2UserInfo ofGoogle(Map<String, Object> attributes, String registrationId) {
        return OAuth2UserInfo.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .provider(registrationId)
                .build();
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes, String registrationId) {
            Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
                .name((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .provider(registrationId)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .email(email)
                .provider(provider)
                .memberKey(KeyGenerator.generateKey())
                .role(Role.USER)
                .build();
    }
}
