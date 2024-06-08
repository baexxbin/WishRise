package com.baexxbin.wishrise.goal.repository;

import com.baexxbin.wishrise.goal.domain.Goal;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Goal goal) {
        if (goal.getId() == null) {
            em.persist(goal);
        } else {
            em.merge(goal);
        }
    }

    public Goal findOne(Long id) {
        return em.find(Goal.class, id);
    }

    public List<Goal> findAll() {
        return em.createQuery("select g from Goal g", Goal.class)
                .getResultList();
    }
}
