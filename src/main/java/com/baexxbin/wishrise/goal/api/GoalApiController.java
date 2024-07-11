package com.baexxbin.wishrise.goal.api;

import com.baexxbin.wishrise.goal.application.GoalService;
import com.baexxbin.wishrise.goal.dto.request.GoalSaveDto;
import com.baexxbin.wishrise.goal.dto.request.GoalServiceDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "GoalApi")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goal")
public class GoalApiController {

    private final GoalService goalService;

    // 목표 저장
    @PostMapping
    public ResponseEntity<Long> createGoal(@RequestParam(name = "loginUserId") Long loginUserId, @Valid @RequestBody GoalSaveDto goalSaveDto) {
        GoalServiceDto goalServiceDto = goalSaveDto.toServiceDto();
        Long goalId = goalService.createGoal(goalServiceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(goalId);
    }
}
