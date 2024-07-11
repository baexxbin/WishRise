package com.baexxbin.wishrise.goal.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("Want")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class ToWant extends Goal {
    private Integer price;

    public ToWant(String title, String description, LocalDate targetDate, Integer price) {
        super(GoalType.WANT, title, description, targetDate);
        this.price = price;
    }

    public void updateToWant(String title, String description, LocalDate targetDate, Integer price) {
        this.updateGoal(GoalType.WANT, title, description, targetDate);
        this.price = price;
    }
}
