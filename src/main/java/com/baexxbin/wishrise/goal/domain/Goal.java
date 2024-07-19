package com.baexxbin.wishrise.goal.domain;

import com.baexxbin.wishrise.register.domain.Register;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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
@SuperBuilder(toBuilder = true)
public class Goal {
    @Id
    @GeneratedValue
    @Column(name = "goal_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private GoalType goalType = GoalType.DO;
    private String title;
    private String description;

    private LocalDate targetDate;
    @Builder.Default
    private int heartCount = 0;

    @JsonIgnore
    @OneToOne(mappedBy = "goal", fetch = LAZY)
    private Register register;


    // ToDo 기본 생성자
    protected Goal(String title, String description, LocalDate targetDate) {
        this.title = title;
        this.description = description;
        this.targetDate = targetDate;
    }


    // ToWant 자식생성자 호출 시 사용
    protected Goal(GoalType goalType, String title, String description, LocalDate targetDate) {
        this.goalType = goalType;
        this.title = title;
        this.description = description;
        this.targetDate = targetDate;
    }

    public void updateGoal(GoalType goalType, String title, String description, LocalDate targetDate) {
        this.goalType = goalType;
        this.title = title;
        this.description = description;
        this.targetDate = targetDate;
    }

}
