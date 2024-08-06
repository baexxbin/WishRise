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

//    @Column(nullable = false, updatable = false)
    private String nickname;

    @Column(nullable = false)
    private String name;
//    @Column(nullable = false)
    @Column()
    private String password;
    @Column(nullable = false, unique = true)
    private String email;

    // 사용자가 인증한 OAuth 제공자
    @Column(nullable = false)
    private String provider;

    @Embedded
    private Information information;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", nullable = false)
    private Role role = Role.USER;

    // 회원 고유 식별 키
    @Column(nullable = false, unique = true)
    private String memberKey;

    @JsonIgnore     // 클래스 속성 수준에서 직렬, 역직렬화시 해당 속성 무시
    @OneToMany(mappedBy = "member")
    private List<Register> resgisters = new ArrayList<>();

    @Column(name = "del_yn")
    private boolean deleted = false;

    @Builder
    public Member(String nickname, String name, String password, String email, String provider, Information information, Role role, String memberKey) {
        this.nickname = nickname;
        this.name = name;
        this.password = password;
        this.email = email;
        this.provider = provider;
        this.information = information;
        this.role = role;
        this.memberKey = memberKey;
    }
    protected Member() {

    }

    public void edit(MemberInfoDto memberInfoDto) {
        this.name = memberInfoDto.getName();
        this.email = memberInfoDto.getEmail();
        this.password = memberInfoDto.getPassword();
    }
}
