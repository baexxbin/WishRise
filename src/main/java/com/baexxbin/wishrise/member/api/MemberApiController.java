package com.baexxbin.wishrise.member.api;

import com.baexxbin.wishrise.member.application.MemberComponentService;
import com.baexxbin.wishrise.member.dto.request.MemberInfoDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "MemberApi")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberComponentService memberComponentService;

    // 회원가입
    @PostMapping
    public ResponseEntity<Long> signUp(@RequestBody MemberInfoDto memberInfoDto) {
        Long memberId = memberComponentService.join(memberInfoDto);
        return ResponseEntity.ok(memberId);
    }


    @PutMapping("{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMember(@PathVariable Long userId, @RequestBody MemberInfoDto memberInfoDto) {
        memberComponentService.update(userId, memberInfoDto);
    }

    // 아이디 중복확인
}
