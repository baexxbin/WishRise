package com.baexxbin.wishrise.register;

import com.baexxbin.wishrise.goal.domain.GoalType;
import com.baexxbin.wishrise.member.application.MemberModuleService;
import com.baexxbin.wishrise.member.domain.Information;
import com.baexxbin.wishrise.member.domain.Member;
import com.baexxbin.wishrise.member.domain.Rank;
import com.baexxbin.wishrise.member.repository.MemberJpaRepository;
import com.baexxbin.wishrise.register.dto.request.RegisterGoalDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RegisterIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberJpaRepository memberJpaRepository;


    @MockBean
    private MemberModuleService memberModuleService;

    private Member member;

    @BeforeEach
    public void setUp() {
        member = memberJpaRepository.save(Member.builder()
                .nickname("testuser")
                .name("Test User")
                .password("Password123!")
                .email("test@example.com")
                .information(new Information(0, Rank.BRONZE))
                .build());
    }

    @Test
    public void testRegisterGoal() throws Exception {
        RegisterGoalDto registerGoalDto = new RegisterGoalDto(
                GoalType.DO,
                "Test Goal",
                "This is a test goal",
                LocalDate.now().plusDays(10),
                null
        );

        given(memberModuleService.findById(any(Long.class))).willReturn(Optional.of(member));

        mockMvc.perform(post("/api/registrations")
                        .param("loginUserId", String.valueOf(member.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerGoalDto)))
                .andExpect(status().isCreated());
    }

}
