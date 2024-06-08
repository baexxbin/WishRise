package com.baexxbin.wishrise.register.domain;

import com.baexxbin.wishrise.goal.domain.Goal;
import com.baexxbin.wishrise.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

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
    @JoinColumn(name = "item_id")
    private Goal goal;

    private LocalDateTime registrationDate;


    @Enumerated(EnumType.STRING)
    private RegisteredStatus status;

}
