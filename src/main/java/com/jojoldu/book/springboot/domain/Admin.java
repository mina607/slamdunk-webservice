// src/main/java/com/jojoldu/book/springboot/domain/Admin.java

package com.jojoldu.book.springboot.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 💡 여기를 수정해야 합니다. (javax -> jakarta)
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Getter
@NoArgsConstructor
@Entity // 이 클래스가 DB 테이블과 매핑됨을 나타냅니다.
public class Admin {

    @Id // 기본 키(Primary Key) 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성 전략 (자동 증가)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 관리자 ID

    @Column(nullable = false)
    private String password; // 암호화된 비밀번호

    // @Builder: 객체 생성을 도와주는 롬복 어노테이션
    @Builder
    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }
}