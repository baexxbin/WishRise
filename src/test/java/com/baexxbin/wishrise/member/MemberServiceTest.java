package com.baexxbin.wishrise.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.baexxbin.wishrise.member.application.MemberService;
import com.baexxbin.wishrise.member.domain.*;
import com.baexxbin.wishrise.member.dto.request.MemberInfoDto;
import com.baexxbin.wishrise.member.repository.MemberJpaRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;


public class MemberServiceTest {
    @Mock
    private MemberJpaRepository memberJpaRepository;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void join() {
        // given
        MemberInfoDto dto = MemberInfoDto.builder()
                .nickname("testNickname")
                .name("testName")
                .password("testPassword")
                .email("test@example.com")
                .build();
        when(memberJpaRepository.findByNickname(dto.getNickname())).thenReturn(Optional.empty());

        Member member = Member.builder()
                .nickname(dto.getNickname())
                .name(dto.getName())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .information(new Information(0, Rank.BRONZE))
                .build();


        // member id값 임시 생성(db와 상호작용안하기 때문)
        ReflectionTestUtils.setField(member, "id", 1L);

        when(memberJpaRepository.save(any(Member.class))).thenReturn(member);

        // when
        Long memberId = memberService.join(dto);

        // then
        assertNotNull(memberId);
        verify(memberJpaRepository, times(1)).save(any(Member.class));
        verify(memberJpaRepository, times(1)).findByNickname(dto.getNickname());

    }

    @Test
    public void testJoinWithDuplicateNickname() {
        // Arrange
        MemberInfoDto dto = MemberInfoDto.builder()
                .nickname("testNickname")
                .name("testName")
                .password("testPassword")
                .email("test@example.com")
                .build();

        Member existingMember = Member.builder()
                .nickname(dto.getNickname())
                .name(dto.getName())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .information(new Information(0, Rank.BRONZE))
                .build();

        when(memberJpaRepository.findByNickname(dto.getNickname())).thenReturn(Optional.of(existingMember));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> memberService.join(dto));

        assertEquals("이미 존재하는 회원입니다.", exception.getMessage());
        verify(memberJpaRepository, never()).save(any(Member.class));
    }
}
