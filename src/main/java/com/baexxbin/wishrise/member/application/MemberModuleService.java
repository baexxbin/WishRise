package com.baexxbin.wishrise.member.application;

import com.baexxbin.wishrise.auth.dto.OAuth2UserInfo;
import com.baexxbin.wishrise.member.domain.Member;
import com.baexxbin.wishrise.member.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberModuleService {

    private final MemberJpaRepository memberJpaRepository;

    @Transactional
    public Long save(Member member) {
        Member saveMember = memberJpaRepository.save(member);
        return saveMember.getId();
    }

    @Transactional
    public Member saveEntity(Member member){
        return memberJpaRepository.save(member);
    }

    public Optional<Member> findById(Long memberId) {
        return memberJpaRepository.findById(memberId);
    }

    public Optional<Member> findByNickname(String nickname) {
        return memberJpaRepository.findByNickname(nickname);
    }

    public Optional<Member> findByEmail(String email) {
        return memberJpaRepository.findByEmail(email);
    }

    public List<Member> findAll() {
        return memberJpaRepository.findAll();
    }

    public Member loginOrJoin(OAuth2UserInfo oAuth2UserInfo) {
        Member member = memberJpaRepository.findByEmail(oAuth2UserInfo.email())
                .orElseGet(oAuth2UserInfo::toEntity);
        return memberJpaRepository.save(member);
    }
}
