package com.baexxbin.wishrise.member.application;

import com.baexxbin.wishrise.member.domain.Information;
import com.baexxbin.wishrise.member.domain.Member;
import com.baexxbin.wishrise.member.domain.Rank;
import com.baexxbin.wishrise.member.dto.request.MemberInfoDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberComponentService {

    private final MemberModuleService memberModuleService;

    @Transactional
    public Long join(MemberInfoDto memberInfoDto) {
        validateDuplicateMemberNickname(memberInfoDto.getNickname());
        validateDuplicateMemberEmail(memberInfoDto.getEmail());

        Member member = createMember(memberInfoDto);
        return memberModuleService.save(member);
    }

    private void validateDuplicateMemberNickname(String nickname) {
        Optional<Member> findMembers = memberModuleService.findByNickname(nickname);
        if (findMembers.isPresent()){
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }

    private void validateDuplicateMemberEmail(String email) {
        Optional<Member> findEmail = memberModuleService.findByEmail(email);
        if (findEmail.isPresent()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
    }

    private Member createMember(MemberInfoDto memberInfoDto) {
        return Member.builder()
                .nickname(memberInfoDto.getNickname())
                .name(memberInfoDto.getName())
                .password(memberInfoDto.getPassword())
                .email(memberInfoDto.getEmail())
                .information(createDefaultInformation())
                .build();
    }

    private Information createDefaultInformation() {
        return Information.builder()
                .point(0)
                .rank(Rank.BRONZE)
                .build();
    }

    @Transactional
    public void update(Long userId, MemberInfoDto memberInfoDto) {
        Optional<Member> optionalMember = memberModuleService.findById(userId);
        Member member = optionalMember.orElseThrow(() -> new EntityNotFoundException("존재하지않는 회원입니다."));
        member.edit(memberInfoDto);
        memberModuleService.save(member);
    }
}
