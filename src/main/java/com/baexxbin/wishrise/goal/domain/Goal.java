package com.baexxbin.wishrise.goal.domain;

import com.baexxbin.wishrise.register.domain.Register;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

import static jakarta.persistence.FetchType.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public abstract class Goal {
    @Id
    @GeneratedValue
    @Column(name = "goal_id")
    private Long id;

    private String title;

    private int heartCount;
    private LocalDate targetDate;

    @JsonIgnore
    @OneToOne(mappedBy = "goal", fetch = LAZY)
    private Register register;


    protected Goal(String title, LocalDate targetDate) {
        this.title = title;
        this.targetDate = targetDate;
        this.heartCount = 0;
    }

    public void updateGoal(String title, LocalDate targetDate) {
        this.title = title;
        this.targetDate = targetDate;
    }

}
