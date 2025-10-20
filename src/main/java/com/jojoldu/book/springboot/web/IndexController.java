package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.config.auth.dto.SessionUser;
import com.jojoldu.book.springboot.domain.order.PaymentType;
import com.jojoldu.book.springboot.service.OrdersService;
import com.jojoldu.book.springboot.service.PostsService;
import com.jojoldu.book.springboot.web.dto.OrderGroupDto;
import com.jojoldu.book.springboot.web.dto.PostsListResponseDto;
import com.jojoldu.book.springboot.web.dto.PostsResponseDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final OrdersService orderService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public  String index(Model model) {

        model.addAttribute("posts", postsService.findAllDesc());
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        return "index" ;
    }

    @GetMapping("/food-delivery")
    public String foodDelivery() {
        return "food-delivery";
    }

    // 주문 처리 추가
//    @PostMapping("/food-delivery/order")
//    public String createOrder(@RequestParam String itemName,
//                              @RequestParam String option,
//                              @RequestParam int quantity,
//                              @RequestParam int price,
//                              @RequestParam String roomNumber,
//                              @RequestParam String phoneNumber) {
//
//        SessionUser user = (SessionUser) httpSession.getAttribute("user");
//        String userId = user != null ? user.getEmail() : "guest";
//
//        // 주문 저장
//        orderService.saveOrder(userId, itemName, option, quantity, price, roomNumber, phoneNumber);
//
//        // 주문 완료 후 주문내역 페이지로 이동
//        return "redirect:/order-status";
//    }

    @PostMapping("/food-delivery/order-multiple")
    @ResponseBody
    public String createMultipleOrders(@RequestBody Map<String, Object> orderData) {
        try {
            SessionUser user = (SessionUser) httpSession.getAttribute("user");
            String userId = user != null ? user.getEmail() : "guest";

            String roomNumber = (String) orderData.get("roomNumber");
            String phoneNumber = (String) orderData.get("phoneNumber");
            String specialRequests = (String) orderData.get("specialRequests");

            // Enum 변환
            String paymentTypeStr = (String) orderData.get("paymentType");
            PaymentType paymentType = PaymentType.valueOf(paymentTypeStr.toUpperCase());

            System.out.println("△△△△△ paymentType △△△△△ : " + paymentType);

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
            orderService.saveMultipleOrders(userId, "food", items, roomNumber, phoneNumber, specialRequests, paymentType);

            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/order-status")
    public String foodOrder(
            @RequestParam(value = "room", required = false, defaultValue = "") String roomNumber,
            Model model) {
        SessionUser user = (SessionUser) httpSession.getAttribute("user");
        String userId = user != null ? user.getEmail() : "guest";

        System.out.println("room number: " + roomNumber);

        List<OrderGroupDto> current = orderService.getCurrentOrdersGrouped(roomNumber);
        List<OrderGroupDto> history = orderService.getCompletedOrdersGrouped(roomNumber);

        System.out.println("=== 주문 내역 조회 ===");
        System.out.println("User ID: " + userId);
        System.out.println("현재 주문 개수: " + current.size());
        System.out.println("완료 주문 개수: " + history.size());

        for (OrderGroupDto group : current) {
            System.out.println("=== OrderGroup: " + group.getOrderNumber() + " ===");
            if (group.getItems() != null) {
                for (OrderGroupDto.OrderItemDto item : group.getItems()) {
                    System.out.println("itemName: " + item.getItemName() + ", icon: " + item.getIcon());
                }
            }
        }


        model.addAttribute("currentOrders", current);
        model.addAttribute("historyOrders", history);

        return "order-status";
    }

    @GetMapping("/article-delivery")
    public String articleDelivery() {
        return "article-delivery";
    }

    @GetMapping("/posts/save")
    public String postSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model){
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
}