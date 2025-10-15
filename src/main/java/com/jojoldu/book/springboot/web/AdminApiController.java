// src/main/java/com/jojoldu/book/springboot/web/AdminApiController.java

package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.web.dto.AdminLoginRequest;
import com.jojoldu.book.springboot.service.AdminAuthService; // 💡 다음 단계에서 만들 서비스
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
// 요청 주소: POST /api/v1/auth/admin
@RequestMapping("/api/v1/auth")
public class AdminApiController {

    private final AdminAuthService adminAuthService;

    // 생성자 주입
    public AdminApiController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }

    @PostMapping("/admin")
    public ResponseEntity<?> adminLogin(@RequestBody AdminLoginRequest request) {

        // 1. 서비스 호출하여 인증 시도
        boolean isAuthenticated = adminAuthService.authenticate(request.getUsername(), request.getPassword());

        if (isAuthenticated) {
            // 2. 인증 성공: 200 OK 반환 (💡 실제로는 세션 생성 로직이 서비스에서 완료되어야 합니다)
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            // 3. 인증 실패: 401 Unauthorized 반환
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }
    }
}