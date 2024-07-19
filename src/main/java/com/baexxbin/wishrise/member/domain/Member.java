package com.baexxbin.wishrise.member.domain;

import com.baexxbin.wishrise.member.dto.request.MemberInfoDto;
import com.baexxbin.wishrise.register.domain.Register;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String nickname;

    private String name;
    private String password;
    private String email;

    @Embedded
    private Information information;

    @JsonIgnore     // 클래스 속성 수준에서 직렬, 역직렬화시 해당 속성 무시
    @OneToMany(mappedBy = "member")
    private List<Register> resgisters = new ArrayList<>();


    @Column(name = "del_yn")
    private boolean deleted = false;

    @Builder
    public Member(String nickname, String name, String password, String email, Information information) {
        this.nickname = nickname;
        this.name = name;
        this.password = password;
        this.email = email;
        this.information = information;
    }
    protected Member() {

    }

    public void edit(MemberInfoDto memberInfoDto) {
        this.name = memberInfoDto.getName();
        this.email = memberInfoDto.getEmail();
        this.password = memberInfoDto.getPassword();
    }
}
