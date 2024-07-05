package com.baexxbin.wishrise.member.repository;

import com.baexxbin.wishrise.member.domain.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepository {
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select  m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByNickname(String name) {
        return em.createQuery("select m from Member m where m.nickname = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

    public void update(Member member) {
        em.merge(member);
    }
}
