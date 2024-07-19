package com.baexxbin.wishrise.goal.dto.request;

import com.baexxbin.wishrise.goal.domain.GoalType;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public abstract class SaveGoalServiceDto {
    private GoalType goalType;
    private String title;
    private String description;
    private LocalDate targetDate;

    public SaveGoalServiceDto(GoalType goalType, String title, String description, LocalDate targetDate) {
        this.goalType = goalType;
        this.title = title;
        this.description = description;
        this.targetDate = targetDate;
    }

    public static class TodoSaveDto extends SaveGoalServiceDto {
        public TodoSaveDto(String title, String description, LocalDate targetDate) {
            super(GoalType.DO, title, description, targetDate);
        }
    }

    @Getter
    public static class TowantSaveDto extends SaveGoalServiceDto {
        private Integer price;

        public TowantSaveDto(String title, String description, LocalDate targetDate, Integer price) {
            super(GoalType.WANT, title, description, targetDate);
            this.price = price;
        }
    }
}
