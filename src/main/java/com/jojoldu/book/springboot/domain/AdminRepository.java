// src/main/java/com/jojoldu/book/springboot/domain/AdminRepository.java

package com.jojoldu.book.springboot.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository를 상속받아 기본적인 DB CRUD 기능을 자동으로 제공받습니다.
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // 💡 쿼리 메소드: 사용자 ID로 관리자 정보를 조회하는 메소드를 자동으로 생성합니다.
    Optional<Admin> findByUsername(String username);
}