package com.baexxbin.wishrise.member;

import com.baexxbin.wishrise.WishriseApplication;
import com.baexxbin.wishrise.member.application.MemberService;
import com.baexxbin.wishrise.member.dto.request.MemberInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = WishriseApplication.class)
@ActiveProfiles("test")
@Transactional
public class MemberServiceIntegrationTest {
    @Autowired
    private MemberService memberService;

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
        Long memberId = memberService.join(dto);

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
        memberService.join(dto1);

        // Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> memberService.join(dto2));

        assertEquals("이미 존재하는 회원입니다.", exception.getMessage());
    }
}
