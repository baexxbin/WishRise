package com.baexxbin.wishrise.member;

import com.baexxbin.wishrise.member.application.MemberComponentService;
import com.baexxbin.wishrise.member.application.MemberModuleService;
import com.baexxbin.wishrise.member.domain.Information;
import com.baexxbin.wishrise.member.domain.Member;
import com.baexxbin.wishrise.member.domain.Rank;
import com.baexxbin.wishrise.member.dto.request.MemberInfoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MemberServiceTest {
    @InjectMocks
    private MemberComponentService memberComponentService;

    @Mock
    private MemberModuleService memberModuleService;

    // @Mock 어노테이션이 붙은 필드들 초기화
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testJoin_Success() {
        // Given: 테스트 데이터 준비
        MemberInfoDto memberInfoDto = MemberInfoDto.builder()
                .nickname("또비닝")
                .name("배수빈")
                .password("Password123!")
                .email("test@example.com")
                .build();

        // Mock 동작 정의
        when(memberModuleService.findByNickname(anyString())).thenReturn(Optional.empty());
        when(memberModuleService.findByEmail(anyString())).thenReturn(Optional.empty());
        when(memberModuleService.save(any(Member.class))).thenReturn(1L);

        // When: 테스트 메서드 호출
        Long memberId = memberComponentService.join(memberInfoDto);

        // Then: 결과 검증
        assertNotNull(memberId);
        verify(memberModuleService).save(any(Member.class));

    }

    @Test
    void testUpdate_Success() {
        // Given
        Long userId = 1L;
        MemberInfoDto memberInfoDto = MemberInfoDto.builder()
                .nickname("또비닝")
                .name("배수빈")
                .password("Password123!")
                .email("test@example.com")
                .build();

        Member member = Member.builder()
                .nickname("또비닝")
                .name("옛날배수빈")
                .password("OldPassword123!")
                .email("test@example.com")
                .information(new Information(0, Rank.BRONZE))
                .build();

        when(memberModuleService.findById(userId)).thenReturn(Optional.of(member));

        // When
        memberComponentService.update(userId, memberInfoDto);

        // Then
        verify(memberModuleService).save(member);
        assertEquals("또비닝", member.getNickname());
        assertEquals(member.getName(), memberInfoDto.getName());
        assertEquals(member.getPassword(), memberInfoDto.getPassword());
        assertEquals(member.getEmail(), memberInfoDto.getEmail());
    }
}
