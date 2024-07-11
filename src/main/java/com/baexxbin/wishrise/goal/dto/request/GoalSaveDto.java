package com.baexxbin.wishrise.goal.dto.request;

import com.baexxbin.wishrise.goal.domain.GoalType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Schema(title = "GOAL_REQ_01 : 버킷 저장 DTO")
public class GoalSaveDto {
    private GoalType goalType;
    private String title;
    private String description;
    private LocalDate targetDate;

    // ToWant일 경우
    private Integer price;


    public GoalSaveDto(GoalType goalType, String title, String description, LocalDate targetDate, Integer price) {
        this.goalType = goalType;
        this.title = title;
        this.description = description;
        this.targetDate = targetDate;
        this.price = price;
    }

    /**
     * 컨트롤러와 서비스 사이 통신용 ServiceDto로 변환하여 전달
     * GoalType에 따라 service에서 처리할 수 있도록 전달
     */
    public GoalServiceDto toServiceDto() {
        if (GoalType.DO.equals(this.goalType)) {
            return new GoalServiceDto.TodoDto(this.title, this.description, this.targetDate);
        } else {
            return new GoalServiceDto.TowantDto(this.title, this.description, this.targetDate, this.price);
        }
    }

}
