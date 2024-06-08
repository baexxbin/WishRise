package com.baexxbin.wishrise.goal.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("Do")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class ToDo extends Goal {
    private String way;

    public ToDo(String title, LocalDate targetDate, String way) {
        super(title, targetDate);
        this.way = way;
    }

    public void updateTodo(String title, LocalDate targetDate, String way) {
        this.updateGoal(title, targetDate);
        this.way = way;
    }

}
