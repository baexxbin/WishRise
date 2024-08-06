package com.baexxbin.wishrise.auth.repository;

import com.baexxbin.wishrise.auth.domain.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, String> {
    Optional<Token> findByRefreshToken(String refreshToken);
    Optional<Token> findByAccessToken(String accessToken);
    Optional<Token> findByAuthId(String authId);
}
