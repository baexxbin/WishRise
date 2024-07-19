package com.baexxbin.wishrise.register.api;

import com.baexxbin.wishrise.register.application.RegisterComponentService;
import com.baexxbin.wishrise.register.dto.request.RegisterGoalDto;
import com.baexxbin.wishrise.goal.dto.request.SaveGoalServiceDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "RegisterApi")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/registrations")
public class RegisterApiController {

    private final RegisterComponentService registerComponentService;

    @PostMapping
    public ResponseEntity<Long> register(@RequestParam(name = "loginUserId") Long loginUserId, @Valid @RequestBody RegisterGoalDto registerGoalDto) {
        SaveGoalServiceDto saveGoalServiceDto = registerGoalDto.toServiceDto();
        Long registeredGoal = registerComponentService.registerGoal(loginUserId, saveGoalServiceDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(registeredGoal);
    }

}
