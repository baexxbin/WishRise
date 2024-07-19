package com.baexxbin.wishrise.goal.api;

import com.baexxbin.wishrise.goal.application.GoalModuleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "GoalApi")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/goal")
public class GoalApiController {

    private final GoalModuleService goalModuleService;
}
