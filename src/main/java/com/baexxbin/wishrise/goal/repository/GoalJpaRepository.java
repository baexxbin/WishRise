package com.baexxbin.wishrise.goal.repository;

import com.baexxbin.wishrise.goal.domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalJpaRepository extends JpaRepository<Goal, Long> {
}
