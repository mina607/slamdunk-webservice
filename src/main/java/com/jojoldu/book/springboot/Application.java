package com.jojoldu.book.springboot;

import com.jojoldu.book.springboot.domain.Admin;
import com.jojoldu.book.springboot.domain.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

// 스프링 부트의 자동 설정, 스프링 brean 읽기와 생성을 모두 자동
@EnableJpaAuditing  // 이 어노테이션을 추가해야 함
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // 💡 관리자 계정 초기 생성 로직 (서버 시작 시 실행됨)
    @Bean
    public CommandLineRunner initData(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        return (args) -> {

            final String ADMIN_ID = "eddy";
            final String ADMIN_PW = "1234";

            // ID가 "eddy"인 계정이 이미 존재하는지 확인
            if (adminRepository.findByUsername(ADMIN_ID).isEmpty()) {

                // 1. 비밀번호 "1234"를 BCrypt로 암호화
                String encodedPassword = passwordEncoder.encode(ADMIN_PW);

                // 2. Admin 엔티티 생성
                Admin admin = Admin.builder()
                        .username(ADMIN_ID)
                        .password(encodedPassword) // 암호화된 비밀번호 저장
                        .build();

                // 3. DB에 저장
                adminRepository.save(admin);
                System.out.println(">>> 관리자 계정 '" + ADMIN_ID + "'가 성공적으로 DB에 등록되었습니다. (PW: " + ADMIN_PW + ")");
            }
        };
    }
}