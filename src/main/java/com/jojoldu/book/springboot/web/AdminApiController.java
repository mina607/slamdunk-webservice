// src/main/java/com/jojoldu/book/springboot/web/AdminApiController.java

package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.web.dto.AdminLoginRequest;
import com.jojoldu.book.springboot.service.AdminAuthService; // 💡 다음 단계에서 만들 서비스
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.Session;
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
    public ResponseEntity<?> adminLogin(@RequestBody AdminLoginRequest request,
                                        HttpServletRequest httpRequest) {

        // 1. 서비스 호출하여 인증 시도
        boolean isAuthenticated = adminAuthService.authenticate(request.getUsername(), request.getPassword());

        if (isAuthenticated) {
            // 2. 인증 성공: 200 OK 반환
            // HttpServletRequest로 세션 생성
            HttpSession session = httpRequest.getSession(true); // 세션 없으면 새로 생성
            session.setAttribute("admin", request.getUsername());
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            // 3. 인증 실패: 401 Unauthorized 반환
            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/check-session")
    public ResponseEntity<?> checkSession(HttpSession session) {
        String admin = (String) session.getAttribute("admin");

        if (admin == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
        return ResponseEntity.ok(true);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return ResponseEntity.ok(true);
    }

}

