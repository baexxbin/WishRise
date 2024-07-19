package com.baexxbin.wishrise.register.dto.request;

import com.baexxbin.wishrise.goal.domain.GoalType;
import com.baexxbin.wishrise.goal.dto.request.SaveGoalServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Schema(title = "REGISTER_REQ_01 : 입력 목표 저장 DTO")
public class RegisterGoalDto {
    private GoalType goalType;
    private String title;
    private String description;
    private LocalDate targetDate;

    // ToWant일 경우
    private Integer price;


    public RegisterGoalDto(GoalType goalType, String title, String description, LocalDate targetDate, Integer price) {
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
    public SaveGoalServiceDto toServiceDto() {
        if (GoalType.DO.equals(this.goalType)) {
            return new SaveGoalServiceDto.TodoSaveDto(this.title, this.description, this.targetDate);
        } else {
            return new SaveGoalServiceDto.TowantSaveDto(this.title, this.description, this.targetDate, this.price);
        }
    }

}
