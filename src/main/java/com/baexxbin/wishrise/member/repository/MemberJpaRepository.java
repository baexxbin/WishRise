package com.baexxbin.wishrise.member.repository;

import com.baexxbin.wishrise.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByEmail(String email);
//    Optional<Member> findUserByEmailAndProvider(String email, String provider);


}
