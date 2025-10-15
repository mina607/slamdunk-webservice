package com.jojoldu.book.springboot.service;

import com.jojoldu.book.springboot.domain.Admin;
import com.jojoldu.book.springboot.domain.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor // final 필드를 이용한 생성자 자동 주입
@Service
public class AdminAuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 관리자 ID와 비밀번호를 검증합니다.
     * @param username 입력된 ID (예: "eddy")
     * @param rawPassword 입력된 평문 비밀번호 (예: "1234")
     * @return 인증 성공 여부
     */
    public boolean authenticate(String username, String rawPassword) {

        // 1. Repository를 이용해 DB에서 사용자 ID로 관리자 정보를 조회합니다.
        // Optional을 사용하여 NullPointerException 방지
        Admin admin = adminRepository.findByUsername(username)
                .orElse(null);

        // 2. 관리자가 DB에 존재하지 않으면 인증 실패
        if (admin == null) {
            return false;
        }

        // 💡 [핵심] 3. PasswordEncoder를 사용해 입력된 비밀번호(rawPassword)와
        //    DB에 저장된 암호화된 비밀번호(admin.getPassword())를 비교합니다.
        // 'eddy'의 평문 비밀번호 '1234'가 DB의 암호화된 비밀번호와 일치하는지 확인합니다.
        return passwordEncoder.matches(rawPassword, admin.getPassword());
    }
}