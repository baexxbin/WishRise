package com.baexxbin.wishrise.auth.dto;

import com.baexxbin.wishrise.auth.exception.AuthException;
import com.baexxbin.wishrise.global.util.KeyGenerator;
import com.baexxbin.wishrise.member.domain.Information;
import com.baexxbin.wishrise.member.domain.Member;
import com.baexxbin.wishrise.member.domain.Rank;
import com.baexxbin.wishrise.member.domain.Role;
import lombok.Builder;

import java.util.Map;

import static com.baexxbin.wishrise.global.exception.ErrorCode.ILLEGAL_REGISTRATION_ID;

/*
* OAuth2 인증 과정에서 사용자 정보를 캡슐화해 다양한 OAuth 제공자에서 통일된 사용자 정보를 제공하는 데 사용
* - 제공자(Google, Kakao 등)에서 받은 사용자 정보를 표준화
* - 인증 후, DB에 사용자 정보를 저장하거나, 회원 가입 과정에서 사용할 Member 엔티티로 변환
* */

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
        Information information = Information.builder()
                .point(0)
                .rank(Rank.BRONZE)
                .build();

        return Member.builder()
                .name(name)
                .email(email)
                .provider(provider)
                .information(information)
                .role(Role.USER)
                .memberKey(KeyGenerator.generateKey())
                .build();
    }
}
