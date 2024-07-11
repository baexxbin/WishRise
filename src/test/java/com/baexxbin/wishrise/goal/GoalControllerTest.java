package com.baexxbin.wishrise.goal;

import com.baexxbin.wishrise.goal.api.GoalApiController;
import com.baexxbin.wishrise.goal.application.GoalService;
import com.baexxbin.wishrise.goal.domain.GoalType;
import com.baexxbin.wishrise.goal.dto.request.GoalSaveDto;
import com.baexxbin.wishrise.goal.dto.request.GoalServiceDto;
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
    private GoalApiController goalApiController;

    @Mock
    private GoalService goalService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(goalApiController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createGoal() throws Exception {
        GoalSaveDto goalSaveDto = new GoalSaveDto(GoalType.DO, "title", "description", LocalDate.now(), null);
        GoalServiceDto goalServiceDto = goalSaveDto.toServiceDto();

        when(goalService.createGoal(any(GoalServiceDto.class))).thenReturn(1L);

        mockMvc.perform(post("/api/goal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("loginUserId", "1")
                        .content(objectMapper.writeValueAsString(goalSaveDto)))
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
