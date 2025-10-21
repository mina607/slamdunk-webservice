package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.config.auth.dto.SessionUser;
import com.jojoldu.book.springboot.domain.order.OrdersRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class AdminController {

    private final HttpSession httpSession;
    private final OrdersRepository ordersRepository;

    @GetMapping("/admin/index")
    public String Dashboard(Model model) {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        model.addAttribute("todayOrders", ordersRepository.countTodayOrders());
        model.addAttribute("todayRevenue", ordersRepository.sumTodayRevenue());
        model.addAttribute("deliveringCount", ordersRepository.countByStatus("DELIVERING"));

        return "admin/index";
    }
}