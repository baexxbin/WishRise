package com.baexxbin.wishrise.goal;

import com.baexxbin.wishrise.goal.application.GoalModuleService;
import com.baexxbin.wishrise.goal.domain.Goal;
import com.baexxbin.wishrise.goal.domain.GoalType;
import com.baexxbin.wishrise.register.api.RegisterApiController;
import com.baexxbin.wishrise.register.dto.request.RegisterGoalDto;
import com.baexxbin.wishrise.goal.dto.request.SaveGoalServiceDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class GoalControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private RegisterApiController registerApiController;

    @Mock
    private GoalModuleService goalModuleService;


    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(registerApiController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createGoal() throws Exception {
        RegisterGoalDto registerGoalDto = new RegisterGoalDto(GoalType.DO, "title", "description", LocalDate.now(), null);
        SaveGoalServiceDto saveGoalServiceDto = registerGoalDto.toServiceDto();

        Goal mockGoal = Goal.builder()
                .title(saveGoalServiceDto.getTitle())
                .description(saveGoalServiceDto.getDescription())
                .targetDate(saveGoalServiceDto.getTargetDate())
                .build();

        when(goalModuleService.createGoal(any(SaveGoalServiceDto.class))).thenReturn(mockGoal);

        mockMvc.perform(post("/api/registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("loginUserId", "1")
                        .content(objectMapper.writeValueAsString(registerGoalDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(1L));
    }

    @ControllerAdvice
    static class GlobalExceptionHandler {
        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public ResponseEntity<String> handleException(Exception ex) {
            System.err.println("An error occurred: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again later.");
        }
    }
}
