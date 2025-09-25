package com.jojoldu.book.springboot.config.auth;

import com.jojoldu.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration  // @Configuration 어노테이션 추가 필요
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // 람다 방식으로 변경
                .headers(headers ->
                        headers.frameOptions(frameOptions -> frameOptions.disable())  // h2-console 화면을 사용하기 위해
                )
                .authorizeHttpRequests(auth -> auth  // authorizeRequests() → authorizeHttpRequests()
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()  // antMatchers() → requestMatchers()
                        .requestMatchers("/api/v1/**").hasRole(Role.USER.name())
                        .anyRequest().authenticated()
                )
                .logout(logout ->
                        logout.logoutSuccessUrl("/")  // 람다 방식으로 변경
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(customOAuth2UserService)  // 람다 방식으로 변경
                        )
                );

        return http.build();  // SecurityFilterChain 반환
    }
}