package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.config.auth.dto.SessionUser;
import com.jojoldu.book.springboot.service.OrdersService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class AriticleController {

    private final OrdersService orderService;
    private final HttpSession httpSession;


    @PostMapping("/article-delivery/order-multiple")
    @ResponseBody
    public String createMultipleOrders(@RequestBody Map<String, Object> orderData) {
        try {
            SessionUser user = (SessionUser) httpSession.getAttribute("user");
            String userId = user != null ? user.getEmail() : "guest";

            String roomNumber = (String) orderData.get("roomNumber");
            String phoneNumber = (String) orderData.get("phoneNumber");
            String specialRequests = (String) orderData.get("specialRequests");

            List<Map<String, Object>> itemMaps = (List<Map<String, Object>>) orderData.get("items");

            // Map을 OrderItem 객체로 변환 (반복문 사용)
            List<OrdersService.OrderItem> items = new ArrayList<>();
            for (Map<String, Object> itemMap : itemMaps) {
                String name = (String) itemMap.get("name");
                int quantity = ((Number) itemMap.get("quantity")).intValue();
                int price = ((Number) itemMap.get("price")).intValue();

                String icon = (String) itemMap.get("icon");

                System.out.println("△△△△△ icon △△△△△ : " + icon);

                items.add(new OrdersService.OrderItem(name, quantity, price, icon));
            }

            // 한 번에 저장 (같은 주문번호로!)
            orderService.saveMultipleOrders(userId, "article", items, roomNumber, phoneNumber, specialRequests, null);

            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }


}
