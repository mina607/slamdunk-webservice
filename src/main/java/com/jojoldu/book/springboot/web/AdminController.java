package com.jojoldu.book.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jojoldu.book.springboot.config.auth.dto.SessionUser;
import com.jojoldu.book.springboot.domain.order.OrdersRepository;
import com.jojoldu.book.springboot.service.AdminService;
import com.jojoldu.book.springboot.web.dto.OrderGroupDto;
import com.jojoldu.book.springboot.web.dto.PopularMenuDto;
import com.jojoldu.book.springboot.web.dto.HourlyOrderDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class AdminController {

    private final HttpSession httpSession;
    private final OrdersRepository ordersRepository;
    private final AdminService adminService;
    private final ObjectMapper objectMapper;

    // 대시보드
    @GetMapping("/admin/index")
    public String Dashboard(Model model) throws Exception {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        // 통계 데이터 (음식 / 물품 분리)
        Long foodOrders = ordersRepository.countFoodOrders();
        Long productOrders = ordersRepository.countProductOrders();
        Long totalOrders = foodOrders + productOrders;

        model.addAttribute("foodOrders", foodOrders);
        model.addAttribute("productOrders", productOrders);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("todayRevenue", ordersRepository.sumTodayRevenue());
        model.addAttribute("deliveringCount", ordersRepository.countByStatus("DELIVERING"));

        // 시간대별 주문 데이터
        List<Object[]> foodResults = ordersRepository.findHourlyFoodOrders();
        List<Object[]> productResults = ordersRepository.findHourlyProductOrders();

        // 음식 주문 데이터를 Map으로 변환 (시간 → 건수)
        Map<Integer, Long> foodMap = new HashMap<>();
        for (Object[] result : foodResults) {
            Integer hour = (Integer) result[0];
            Long count = ((Number) result[1]).longValue();
            foodMap.put(hour, count);
        }

        // 물품 주문 데이터를 Map으로 변환
        Map<Integer, Long> productMap = new HashMap<>();
        for (Object[] result : productResults) {
            Integer hour = (Integer) result[0];
            Long count = ((Number) result[1]).longValue();
            productMap.put(hour, count);
        }

        // 0시부터 23시까지 데이터 생성 (없는 시간은 0으로)
        List<HourlyOrderDto> hourlyOrders = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            Long foodCount = foodMap.getOrDefault(hour, 0L);
            Long productCount = productMap.getOrDefault(hour, 0L);
            hourlyOrders.add(new HourlyOrderDto(hour, foodCount, productCount));
        }

        model.addAttribute("hourlyOrders", hourlyOrders);

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

        // JSON 문자열로 변환
        String hourlyOrdersJson = objectMapper.writeValueAsString(hourlyOrders);
        model.addAttribute("hourlyOrdersJson", hourlyOrdersJson);

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

        model.addAttribute("pendingCount", pendingCount);

        model.addAttribute("orders", orders);
        model.addAttribute("status", status); // 현재 필터 상태 유지용

        // 사이드바 메뉴 활성화
        model.addAttribute("isOrderManagement", true);

        return "admin/order-management";
    }

}