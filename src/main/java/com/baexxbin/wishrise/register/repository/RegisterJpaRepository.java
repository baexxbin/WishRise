package com.baexxbin.wishrise.register.repository;

import com.baexxbin.wishrise.register.domain.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterJpaRepository extends JpaRepository<Register, Long> {
}
