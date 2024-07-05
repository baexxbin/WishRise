package com.baexxbin.wishrise.member.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Embeddable
@Getter
public class Information {
    private Integer point;

    @Column(name = "member_rank")       // rank는 db예약어
    @Enumerated(EnumType.STRING)
    private Rank rank;

    @Builder
    public Information(Integer point, Rank rank) {
        this.point = point;
        this.rank = rank;
    }

    protected Information() {   // 임베디드 타입 기본생성자 필수

    }
}
