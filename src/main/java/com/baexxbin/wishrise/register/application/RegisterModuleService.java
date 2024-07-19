package com.baexxbin.wishrise.register.application;

import com.baexxbin.wishrise.register.domain.Register;
import com.baexxbin.wishrise.register.repository.RegisterJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RegisterModuleService {

    private final RegisterJpaRepository registerJpaRepository;

    public Register save(Register register) {
        return registerJpaRepository.save(register);
    }
}
