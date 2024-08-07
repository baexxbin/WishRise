package com.baexxbin.wishrise.member.application;

import com.baexxbin.wishrise.member.domain.Information;
import com.baexxbin.wishrise.member.domain.Member;
import com.baexxbin.wishrise.member.domain.Rank;
import com.baexxbin.wishrise.member.dto.request.MemberInfoDto;
import com.baexxbin.wishrise.member.repository.MemberJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberJpaRepository memberJpaRepository;

    // 회원가입
    @Transactional
    public Long join(MemberInfoDto memberInfoDto) {
        validateDuplicateMember(memberInfoDto.getNickname());

        Member member = createMember(memberInfoDto);
        Member saveMember = memberJpaRepository.save(member);

        return saveMember.getId();
    }

    private void validateDuplicateMember(String nickname) {
        Optional<Member> findMembers = memberJpaRepository.findByNickname(nickname);
        if (findMembers.isPresent()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
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

    //회원 전체조회
    public List<Member> findMembers() {
        return memberJpaRepository.findAll();
    }

    // 회원 단일 조회
    public Optional<Member> find(Long memberId) {
        return memberJpaRepository.findById(memberId);
    }


    // 회원 업데이트
    @Transactional
    public void update(Long userId, MemberInfoDto memberInfoDto) {
        Optional<Member> optionalMember = find(userId);
        Member member = optionalMember.orElseThrow(() -> new EntityNotFoundException("해당 ID의 회원이 존재하지 않습니다."));
        member.edit(memberInfoDto);
        memberJpaRepository.save(member);
    }
}
