package com.baexxbin.wishrise.member.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoDto {
    private String nickname;
    private String name;
    private String password;
    private String email;

    public MemberInfoDto(String nickname, String name, String password, String email) {
        this.nickname = nickname;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    protected MemberInfoDto(){}
}
