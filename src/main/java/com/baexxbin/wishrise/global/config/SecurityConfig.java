package com.baexxbin.wishrise.global.config;

import com.baexxbin.wishrise.auth.handler.CustomAccessDeniedHandler;
import com.baexxbin.wishrise.auth.handler.CustomAuthenticationEntryPoint;
import com.baexxbin.wishrise.auth.handler.OAuth2FailureHandler;
import com.baexxbin.wishrise.auth.handler.OAuth2SuccessHandler;
import com.baexxbin.wishrise.auth.jwt.TokenAuthenticationFilter;
import com.baexxbin.wishrise.auth.jwt.TokenExceptionFilter;
import com.baexxbin.wishrise.auth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // security를 적용하지 않을 리소스
        return web -> web.ignoring()
                .requestMatchers("/error", "/favicon.ico");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest
                                .requestMatchers(
                                        AntPathRequestMatcher.antMatcher("/api/members"),
                                        AntPathRequestMatcher.antMatcher("/api/login"),
                                        AntPathRequestMatcher.antMatcher("/auth/success"),
                                        AntPathRequestMatcher.antMatcher("/swagger-resources/**"),
                                        AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
                                        AntPathRequestMatcher.antMatcher("/token/**")   // 토큰 발급 경로 허용
                                ).permitAll()
                                .anyRequest().authenticated()
                )
                // oauth2 설정
                .oauth2Login(oauth ->
                        oauth
                                .userInfoEndpoint(c ->
                                        c.userService(customOAuth2UserService)
                                )
                                .successHandler(oAuth2SuccessHandler)
                                .failureHandler(new OAuth2FailureHandler())
                )

                // jwt 관련 설정
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new TokenExceptionFilter(), tokenAuthenticationFilter.getClass()) // 토큰 예외 핸들링

                // 인증 예외 핸들링
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler()));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
