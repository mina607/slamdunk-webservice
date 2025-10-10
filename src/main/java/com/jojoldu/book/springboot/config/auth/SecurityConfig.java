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
                .authorizeHttpRequests(auth -> auth
                        // 고객용 페이지는 로그인 없이 접근 허용
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/food-delivery/**", "/admin/**").permitAll()

                        // 관리자 페이지만 로그인 필요
                        //.requestMatchers("/admin/**").authenticated()

                        // 나머지 경로는 모두 허용 (또는 필요하면 authenticated())
                        .anyRequest().permitAll()
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