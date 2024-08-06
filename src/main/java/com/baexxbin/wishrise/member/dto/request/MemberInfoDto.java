package com.baexxbin.wishrise.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "MEM_REQ_01 : 회원가입 요청 DTO")
public class MemberInfoDto {

    @NotBlank(message = "사용자 닉네임을 입력해주세요.")
    @Size(min = 3, max = 15, message = "사용자 닉네임은 15글자 이하로 입력해야 합니다.")
    private String nickname;

    @NotBlank(message = "사용자 이름을 입력해주세요.")
    @Size(min = 3, max = 15, message = "사용자 이름은 15글자 이하로 입력해야 합니다.")
    private String name;

    @NotBlank
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    @NotBlank(message = "사용자 이메일을 입력해주세요.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;



    public MemberInfoDto(String nickname, String name, String password, String email) {
        this.nickname = nickname;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    protected MemberInfoDto(){}
}
