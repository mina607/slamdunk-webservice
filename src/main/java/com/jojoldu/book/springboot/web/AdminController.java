package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.config.auth.dto.SessionUser;
import com.jojoldu.book.springboot.domain.order.OrdersRepository;
import com.jojoldu.book.springboot.service.AdminService;
import com.jojoldu.book.springboot.web.dto.OrderGroupDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AdminController {

    private final HttpSession httpSession;
    private final OrdersRepository ordersRepository;
    private final AdminService adminService;

    // 대시보드
    @GetMapping("/admin/index")
    public String Dashboard(Model model) {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        model.addAttribute("todayOrders", ordersRepository.countTodayOrders());
        model.addAttribute("todayRevenue", ordersRepository.sumTodayRevenue());
        model.addAttribute("deliveringCount", ordersRepository.countByStatus("DELIVERING"));

        // 사이드바 메뉴 활성화
        model.addAttribute("isDashboard", true);
        return "admin/index";
    }

    // 주문 관리
    @GetMapping("/admin/order-management")
    public String orderManagement(
            @RequestParam(value = "status", required = false, defaultValue = "") String status,
            Model model) {

        List<OrderGroupDto> orders = adminService.getOrdersByStatus(status);


        model.addAttribute("orders", orders);
        model.addAttribute("status", status); // 현재 필터 상태 유지용

        // 사이드바 메뉴 활성화
        model.addAttribute("isOrderManagement", true);

        return "admin/order-management";
    }

}