package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.config.auth.dto.SessionUser;
import com.jojoldu.book.springboot.domain.order.OrdersRepository;
import com.jojoldu.book.springboot.service.AdminService;
import com.jojoldu.book.springboot.web.dto.OrderGroupDto;
import com.jojoldu.book.springboot.web.dto.PopularMenuDto;  // ← 추가
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.ArrayList;  // ← 추가

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

        // 통계 데이터
        model.addAttribute("todayOrders", ordersRepository.countTodayOrders());
        model.addAttribute("todayRevenue", ordersRepository.sumTodayRevenue());
        model.addAttribute("deliveringCount", ordersRepository.countByStatus("DELIVERING"));

        // 인기 메뉴 데이터 추가 (물품 제외, 음식만)
        List<Object[]> results = ordersRepository.findPopularMenus();
        List<PopularMenuDto> popularMenus = new ArrayList<>();

        int rank = 1;
        for (Object[] result : results) {
            if (rank > 5) break;  // 상위 5개만

            String menuName = (String) result[0];
            String category = (String) result[2];
            Long orderCount = ((Number) result[3]).longValue();

            popularMenus.add(new PopularMenuDto(rank++, menuName, category, orderCount));
        }

        model.addAttribute("popularMenus", popularMenus);

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

        // 상태별 개수 계산
        long pendingCount = orders.stream()
                .mapToLong(o -> o.getCountByStatus("ORDERED"))
                .sum();

        long preparedCount = orders.stream()
                .mapToLong(o -> o.getCountByStatus("PREPARED"))
                .sum();


        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("preparedCount", preparedCount);

        model.addAttribute("orders", orders);
        model.addAttribute("status", status); // 현재 필터 상태 유지용

        // 사이드바 메뉴 활성화
        model.addAttribute("isOrderManagement", true);

        return "admin/order-management";
    }

}