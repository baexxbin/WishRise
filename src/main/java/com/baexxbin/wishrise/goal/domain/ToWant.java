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
    private String reason;
    private int price;

    public ToWant(String title, LocalDate targetDate, String reason, int price) {
        super(title, targetDate);
        this.reason = reason;
        this.price = price;
    }

    public void updateToWant(String title, LocalDate targetDate, String reason, int price) {
        this.updateGoal(title, targetDate);
        this.reason = reason;
        this.price = price;
    }
}
