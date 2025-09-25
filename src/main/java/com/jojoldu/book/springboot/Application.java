package com.jojoldu.book.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// 스프링 부트의 자동 설정, 스프링 brean 읽기와 생성을 모두 자동
@SpringBootApplication
@EnableJpaAuditing  // 이 어노테이션을 추가해야 함
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }
}