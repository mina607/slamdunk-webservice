package com.jojoldu.book.springboot.config.auth;

import com.jojoldu.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // 💡 추가
import org.springframework.security.crypto.password.PasswordEncoder; // 💡 추가

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    // 💡 1. PasswordEncoder 빈 등록 (DB 인증을 위해 필수)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers ->
                        headers.frameOptions(frameOptions -> frameOptions.disable()) // h2-console을 위해
                )
                .authorizeHttpRequests(auth -> auth
                        // 💡 2. /admin/api/auth 경로도 로그인 없이 접근 허용해야 합니다.
                        // 💡 3. 관리자 로그인을 테스트할 수 있도록 /admin/** 경로도 잠시 허용(permitAll)합니다.
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/food-delivery/**", "/api/v1/auth/admin", "/admin/**").permitAll()

                        // 고객용 페이지는 로그인 없이 접근 허용
                        // .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/food-delivery/**").permitAll()

                        // ⚠️ 주의: 관리자 페이지는 세션 생성 후 접근 통제를 다시 설정해야 합니다.
                        // .requestMatchers("/admin/**").authenticated() // 현재는 주석 처리 또는 제거

                        // 나머지 경로는 모두 허용 (또는 필요하면 authenticated())
                        .anyRequest().permitAll()
                )
                // 💡 4. 폼 로그인 관련 설정 (관리자 로그인에 대비)
                .formLogin(formLogin -> formLogin
                        .disable() // 기본 폼 로그인 비활성화 (API로 처리할 것이므로)
                )

                .logout(logout ->
                        logout.logoutSuccessUrl("/")
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(customOAuth2UserService)
                        )
                );

        return http.build();
    }
}