package com.baexxbin.wishrise.register.application;

import com.baexxbin.wishrise.goal.application.GoalModuleService;
import com.baexxbin.wishrise.goal.domain.Goal;
import com.baexxbin.wishrise.goal.dto.request.SaveGoalServiceDto;
import com.baexxbin.wishrise.member.application.MemberModuleService;
import com.baexxbin.wishrise.member.domain.Member;
import com.baexxbin.wishrise.register.domain.Register;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class RegisterComponentService {

    private final RegisterModuleService registerModuleService;
    private final GoalModuleService goalModuleService;
    private final MemberModuleService memberModuleService;

    @Transactional
    public Long registerGoal(Long loginUserId, SaveGoalServiceDto saveGoalServiceDto) {
        Goal savedGoal = goalModuleService.createGoal(saveGoalServiceDto);

        Optional<Member> registerUser = memberModuleService.findById(loginUserId);
        if (registerUser.isEmpty()) {
            throw new IllegalArgumentException("User not found with id: " + loginUserId);
        }

        Register register = Register.builder()
                .member(registerUser.get())
                .goal(savedGoal)
                .build();
        Register savedRegister = registerModuleService.save(register);
        return savedRegister.getId();

    }
}
