package com.baexxbin.wishrise.member.api;

import com.baexxbin.wishrise.member.application.MemberService;
import com.baexxbin.wishrise.member.dto.request.MemberInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping
    public ResponseEntity<Long> signUp(@RequestBody MemberInfoDto memberInfoDto) {
        Long memberId = memberService.join(memberInfoDto);
        return ResponseEntity.ok(memberId);
    }


    @PutMapping("{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMember(@PathVariable Long userId, @RequestBody MemberInfoDto memberInfoDto) {
        memberService.update(userId, memberInfoDto);
    }
}
