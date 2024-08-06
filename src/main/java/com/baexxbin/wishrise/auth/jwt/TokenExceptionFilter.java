package com.baexxbin.wishrise.auth.jwt;

import com.baexxbin.wishrise.auth.exception.TokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
* 토큰 관련 예외처리를 위한 예외 핸들링 필터
* */

public class TokenExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (TokenException e) {    // servlet으로 예외전달
            response.sendError(e.getErrorCode().getHttpStatus().value(), e.getMessage());
        }
    }
}
