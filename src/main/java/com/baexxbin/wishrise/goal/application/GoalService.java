package com.baexxbin.wishrise.goal.application;

import com.baexxbin.wishrise.goal.domain.Goal;
import com.baexxbin.wishrise.goal.domain.ToWant;
import com.baexxbin.wishrise.goal.dto.request.GoalServiceDto;
import com.baexxbin.wishrise.goal.repository.GoalJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoalService {

    private final GoalJpaRepository goalJpaRepository;


    @Transactional
    public Long createGoal(GoalServiceDto goalServiceDto){
        if (goalServiceDto instanceof GoalServiceDto.TowantDto) {
            return createGoalTowant((GoalServiceDto.TowantDto) goalServiceDto);
        }

        Goal toDo = Goal.builder()
                .title(goalServiceDto.getTitle())
                .description(goalServiceDto.getDescription())
                .targetDate(goalServiceDto.getTargetDate())
                .build();
        Goal toDoGoal = goalJpaRepository.save(toDo);
        return toDoGoal.getId();
    }

    @Transactional
    public Long createGoalTowant(GoalServiceDto.TowantDto goalServiceDto){
        ToWant toWant = ToWant.builder()
                .title(goalServiceDto.getTitle())
                .description(goalServiceDto.getDescription())
                .targetDate(goalServiceDto.getTargetDate())
                .price(goalServiceDto.getPrice())
                .build();
        ToWant toWantGoal = goalJpaRepository.save(toWant);
        return toWantGoal.getId();
    }
}
