package com.baexxbin.wishrise.member;

import com.baexxbin.wishrise.member.application.MemberComponentService;
import com.baexxbin.wishrise.member.domain.Information;
import com.baexxbin.wishrise.member.domain.Member;
import com.baexxbin.wishrise.member.domain.Rank;
import com.baexxbin.wishrise.member.dto.request.MemberInfoDto;
import com.baexxbin.wishrise.member.repository.MemberJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@Transactional
public class MemberServiceIntegrationTest {
    @Autowired
    private MemberComponentService memberComponentService;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
    public void testJoin() {
        // Arrange
        MemberInfoDto dto = MemberInfoDto.builder()
                .nickname("testNickname")
                .name("testName")
                .password("testPassword")
                .email("test@example.com")
                .build();

        // Act
        Long memberId = memberComponentService.join(dto);

        // Assert
        assertNotNull(memberId);
    }

    @Test
    public void testJoinWithDuplicateNickname() {
        // Arrange
        MemberInfoDto dto1 = MemberInfoDto.builder()
                .nickname("배수빙")
                .name("배수빈")
                .password("1234")
                .email("test@example.com")
                .build();

        MemberInfoDto dto2 = MemberInfoDto.builder()
                .nickname("배수빙")
                .name("배수빔")
                .password("12345")
                .email("test2@example.com")
                .build();

        // Act
        memberComponentService.join(dto1);

        // Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> memberComponentService.join(dto2));

        assertEquals("이미 존재하는 아이디입니다.", exception.getMessage());
    }

    @Test
    public void testUpdateMember_Success() {
        // Given
        MemberInfoDto memberInfoDto = MemberInfoDto.builder()
                .nickname("또비닝")
                .name("배수빈")
                .password("Password123!")
                .email("test@example.com")
                .build();

        Member savedMember = memberJpaRepository.save(Member.builder()
                .nickname("또비닝")
                .name("옛날배수빈")
                .password("OldPassword123!")
                .email("test@example.com")
                .information(new Information(0, Rank.BRONZE))
                .build());

        Long userId = savedMember.getId();

        // When
        memberComponentService.update(userId, memberInfoDto);

        // Then
        Optional<Member> updatedMemberOpt = memberJpaRepository.findById(userId);
        Member updatedMember = updatedMemberOpt.orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
        assertEquals(memberInfoDto.getName(), updatedMember.getName());
        assertEquals(memberInfoDto.getPassword(), updatedMember.getPassword());
        assertEquals(memberInfoDto.getEmail(), updatedMember.getEmail());
        assertEquals("또비닝", updatedMember.getNickname());  // 닉네임은 변경되지 않아야 함
    }

}
