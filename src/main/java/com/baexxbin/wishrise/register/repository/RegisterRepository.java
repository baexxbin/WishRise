package com.baexxbin.wishrise.register.repository;

import com.baexxbin.wishrise.member.domain.Member;
import com.baexxbin.wishrise.register.domain.Register;
import com.baexxbin.wishrise.register.dto.RegisterSearch;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RegisterRepository {

    private final EntityManager em;

    public void save(Register register) {
        em.persist(register);
    }

    public Register findOne(Long id) {
        return em.find(Register.class, id);
    }

    public List<Register> findAll(RegisterSearch registerSearch) {
        return em.createQuery("select o from Register o join o.member m" +
                        " where o.status =:status" +
                        " and m.name like :name", Register.class)
                .setParameter("status", registerSearch.getRegisteredStatus())
                .setParameter("name", registerSearch.getMemberName())
                .setMaxResults(1000)    // 최대 1000건
                .getResultList();
    }

    // JPA Criteria
    public List<Register> findAllByCriteria(RegisterSearch registerSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Register> cq = cb.createQuery(Register.class);
        Root<Register> o = cq.from(Register.class);
        Join<Register, Member> m = o.join("member", JoinType.INNER);
        List<Predicate> criteria = new ArrayList<>();
        //주문 상태 검색
        if (registerSearch.getRegisteredStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    registerSearch.getRegisteredStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(registerSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" +
                            registerSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Register> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건
        return query.getResultList();
    }

    /**
     * Register 조회 시 한방 쿼리로 Member, item 한번에 다 가져오기
     */
    public List<Register> findAllWithMemberItem() {
        return em.createQuery(
                "select r from Register r" +
                        " join fetch r.member m" +
                        " join fetch r.goal i", Register.class
        ).getResultList();
    }
}
