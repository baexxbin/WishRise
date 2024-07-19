package com.baexxbin.wishrise.goal;

import com.baexxbin.wishrise.goal.application.GoalModuleService;
import com.baexxbin.wishrise.goal.domain.Goal;
import com.baexxbin.wishrise.goal.domain.ToWant;
import com.baexxbin.wishrise.goal.repository.GoalJpaRepository;
import com.baexxbin.wishrise.goal.dto.request.SaveGoalServiceDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)     // Junit5, Mockito 연동을 위해 작성
public class GoalServiceTest {

    @InjectMocks
    private GoalModuleService goalModuleService;

    @Mock
    private GoalJpaRepository goalJpaRepository;

    @Test
    void createGoal() {
        SaveGoalServiceDto saveGoalServiceDto = new SaveGoalServiceDto.TodoSaveDto("목표 제목", "내용내용", LocalDate.now());

        Goal expectedGoal = Goal.builder()
                .title("목표 제목")
                .description("내용내용")
                .targetDate(LocalDate.now())
                .build();

        when(goalJpaRepository.save(any(Goal.class))).thenReturn(expectedGoal);

        Long goalId = goalModuleService.createGoal(saveGoalServiceDto).getId();
        assertEquals(expectedGoal.getId(), goalId);
    }

    @Test
    void createGoalTowant() {
        SaveGoalServiceDto.TowantSaveDto goalServiceDto = new SaveGoalServiceDto.TowantSaveDto("물건", "갖고싶어요", LocalDate.now(), 1000);

        ToWant expectedToWant = ToWant.builder()
                .title("물건")
                .description("갖고싶어요")
                .targetDate(LocalDate.now())
                .price(1000)
                .build();

        when(goalJpaRepository.save(any(ToWant.class))).thenReturn(expectedToWant);
        Long goalId = goalModuleService.createGoalTowant(goalServiceDto).getId();
        assertEquals(expectedToWant.getId(), goalId);
    }
}
