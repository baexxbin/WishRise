package com.baexxbin.wishrise.member.domain;

import com.baexxbin.wishrise.register.domain.Register;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    private String name;

    @Embedded
    private Information information;

    @JsonIgnore     // 클래스 속성 수준에서 직렬, 역직렬화시 해당 속성 무시
    @OneToMany(mappedBy = "member")
    private List<Register> resgisters = new ArrayList<>();
}
