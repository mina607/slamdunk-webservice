package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.config.auth.dto.SessionUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class AdminController {

    private final HttpSession httpSession;

    @GetMapping("/admin/index")
    public String Dashboard() {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        return "admin/index";
    }
}
