package com.baexxbin.wishrise.register.domain;

import com.baexxbin.wishrise.goal.domain.Goal;
import com.baexxbin.wishrise.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.baexxbin.wishrise.register.domain.RegisteredStatus.*;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "registers")
@Getter
public class Register {
    @Id
    @GeneratedValue
    @Column(name = "register_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "goal_id")
    private Goal goal;

    private LocalDateTime registrationDate;

    @Builder.Default
    private LocalDateTime achievementDate = LocalDateTime.of(1970, 1, 1, 0, 0, 0).atOffset(ZoneOffset.UTC).toLocalDateTime();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private RegisteredStatus status = REGISTRATION;


    @Builder
    public Register(Member member, Goal goal) {
        this.member = member;
        this.goal = goal;
        this.registrationDate = LocalDateTime.now(ZoneOffset.UTC);
    }

    protected Register() {

    }

}
