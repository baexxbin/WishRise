package com.baexxbin.wishrise.goal.application;

import com.baexxbin.wishrise.goal.domain.Goal;
import com.baexxbin.wishrise.goal.domain.ToWant;
import com.baexxbin.wishrise.goal.repository.GoalJpaRepository;
import com.baexxbin.wishrise.goal.dto.request.SaveGoalServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GoalModuleService {

    private final GoalJpaRepository goalJpaRepository;

    public Goal createGoal(SaveGoalServiceDto saveGoalServiceDto) {
        if (saveGoalServiceDto instanceof SaveGoalServiceDto.TowantSaveDto) {
            return createGoalTowant((SaveGoalServiceDto.TowantSaveDto) saveGoalServiceDto);
        }

        Goal toDo = Goal.builder()
                .title(saveGoalServiceDto.getTitle())
                .description(saveGoalServiceDto.getDescription())
                .targetDate(saveGoalServiceDto.getTargetDate())
                .build();
        return goalJpaRepository.save(toDo);
    }


    public ToWant createGoalTowant(SaveGoalServiceDto.TowantSaveDto goalServiceDto) {
        ToWant toWant = ToWant.builder()
                .title(goalServiceDto.getTitle())
                .description(goalServiceDto.getDescription())
                .targetDate(goalServiceDto.getTargetDate())
                .price(goalServiceDto.getPrice())
                .build();
        return goalJpaRepository.save(toWant);
    }
}
