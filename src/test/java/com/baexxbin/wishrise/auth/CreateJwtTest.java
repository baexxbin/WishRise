package com.baexxbin.wishrise.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("oauth")
public class CreateJwtTest {
    @Value("${spring.security.oauth2.resourceserver.jwt.secret}")
    private String secretKeyPlain;

    @Test
    void 시크릿키_존재_확인() {
        System.out.println("ENCRYPT_KEY: " + System.getenv("ENCRYPT_KEY"));
        assertThat(secretKeyPlain).isNotNull();
    }
}
