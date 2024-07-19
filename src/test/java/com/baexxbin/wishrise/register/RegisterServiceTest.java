package com.baexxbin.wishrise.register;

import com.baexxbin.wishrise.goal.application.GoalModuleService;
import com.baexxbin.wishrise.goal.domain.Goal;
import com.baexxbin.wishrise.goal.dto.request.SaveGoalServiceDto;
import com.baexxbin.wishrise.member.application.MemberModuleService;
import com.baexxbin.wishrise.member.domain.Information;
import com.baexxbin.wishrise.member.domain.Member;
import com.baexxbin.wishrise.member.domain.Rank;
import com.baexxbin.wishrise.register.application.RegisterComponentService;
import com.baexxbin.wishrise.register.application.RegisterModuleService;
import com.baexxbin.wishrise.register.domain.Register;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RegisterServiceTest {
    @InjectMocks
    private RegisterComponentService registerComponentService;

    @Mock
    private RegisterModuleService registerModuleService;

    @Mock
    private GoalModuleService goalModuleService;

    @Mock
    private MemberModuleService memberModuleService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterGoal_Success() {
        // Given
        Long userId = 1L;
        SaveGoalServiceDto saveGoalServiceDto = new SaveGoalServiceDto.TodoSaveDto(
                "Test Goal",
                "This is a test goal",
                LocalDate.now().plusDays(10)
        );

        Member member = Member.builder()
                .nickname("testuser")
                .name("Test User")
                .password("Password123!")
                .email("test@example.com")
                .information(new Information(0, Rank.BRONZE))
                .build();
        ReflectionTestUtils.setField(member, "id", userId);

        Goal goal = Goal.builder()
                .goalType(saveGoalServiceDto.getGoalType()) // 명시적으로 GoalType 설정
                .title(saveGoalServiceDto.getTitle())
                .description(saveGoalServiceDto.getDescription())
                .targetDate(saveGoalServiceDto.getTargetDate())
                .build();
        ReflectionTestUtils.setField(goal, "id", 1L);

        Register register = Register.builder()
                .member(member)
                .goal(goal)
                .build();

        when(goalModuleService.createGoal(any(SaveGoalServiceDto.class))).thenReturn(goal);
        when(memberModuleService.findById(userId)).thenReturn(Optional.of(member));

        // Mock 객체는 실제 저장소와 상호작용하지 않기에 저장된 ID값 자동 설정 안됨 >> 수동으로 ID값 설정해줌
        when(registerModuleService.save(any(Register.class))).thenAnswer(invocation -> {
            Register arg = invocation.getArgument(0);
            ReflectionTestUtils.setField(arg, "id", 1L);
            return arg;
        });

        // When
        Long registeredGoalId = registerComponentService.registerGoal(userId, saveGoalServiceDto);

        // Then
        assertEquals(1L, registeredGoalId);
    }

    @Test
    public void testRegisterGoal_UserNotFound() {
        // Given
        Long userId = 1L;
        SaveGoalServiceDto saveGoalServiceDto = new SaveGoalServiceDto.TodoSaveDto(
                "Test Goal",
                "This is a test goal",
                LocalDate.now().plusDays(10)
        );

        when(memberModuleService.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                registerComponentService.registerGoal(userId, saveGoalServiceDto));

        assertEquals("User not found with id: " + userId, exception.getMessage());
    }
}
