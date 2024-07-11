package com.baexxbin.wishrise.goal;

import com.baexxbin.wishrise.goal.application.GoalService;
import com.baexxbin.wishrise.goal.domain.Goal;
import com.baexxbin.wishrise.goal.domain.ToWant;
import com.baexxbin.wishrise.goal.dto.request.GoalServiceDto;
import com.baexxbin.wishrise.goal.repository.GoalJpaRepository;
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
    private GoalService goalService;

    @Mock
    private GoalJpaRepository goalJpaRepository;

    @Test
    void createGoal() {
        GoalServiceDto goalServiceDto = new GoalServiceDto.TodoDto("목표 제목", "내용내용", LocalDate.now());

        Goal expectedGoal = Goal.builder()
                .title("목표 제목")
                .description("내용내용")
                .targetDate(LocalDate.now())
                .build();

        when(goalJpaRepository.save(any(Goal.class))).thenReturn(expectedGoal);

        Long goalId = goalService.createGoal(goalServiceDto);
        assertEquals(expectedGoal.getId(), goalId);
    }

    @Test
    void createGoalTowant() {
        GoalServiceDto.TowantDto goalServiceDto = new GoalServiceDto.TowantDto("물건", "갖고싶어요", LocalDate.now(), 1000);

        ToWant expectedToWant = ToWant.builder()
                .title("물건")
                .description("갖고싶어요")
                .targetDate(LocalDate.now())
                .price(1000)
                .build();

        when(goalJpaRepository.save(any(ToWant.class))).thenReturn(expectedToWant);
        Long goalId = goalService.createGoalTowant(goalServiceDto);
        assertEquals(expectedToWant.getId(), goalId);
    }
}
