package com.jojoldu.book.springboot.service;

import com.jojoldu.book.springboot.domain.order.Orders;
import com.jojoldu.book.springboot.domain.order.OrdersRepository;
import com.jojoldu.book.springboot.domain.order.PaymentType;
import com.jojoldu.book.springboot.web.dto.OrderGroupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;

    @Transactional(readOnly = true)
    public List<Orders> getCurrentOrders(String userId) {
        return ordersRepository.findCurrentOrdersByRoom (userId);
    }

    @Transactional(readOnly = true)
    public List<Orders> getCompletedOrders(String userId) {
        return ordersRepository.findCompletedOrdersByRoom(userId);
    }

    // 주문 저장 메서드 추가
    @Transactional
    public Long saveOrder(String userId, String itemName, String option, int quantity,
                          int price, String roomNumber, String phoneNumber) {

        // 주문번호 생성 (날짜 + 랜덤)
        String orderNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" + String.format("%03d", (int)(Math.random() * 1000));

        // 주문시간
        String orderTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));

        Orders order = Orders.builder()
                .orderNumber(orderNumber)
                .userId(userId)
                .itemName(itemName)
                .category(getCategoryByMenuItem(itemName))  // ← 추가!
                .option(option)
                .quantity(quantity)
                .price(price)
                .roomNumber(roomNumber)
                .phoneNumber(phoneNumber)
                .status("ORDERED")
                .orderTime(orderTime)
                .build();

        return ordersRepository.save(order).getId();
    }

    @Transactional
    public void saveMultipleOrders(String userId, String option, List<OrderItem> items,
                                   String roomNumber, String phoneNumber, String specialRequests, PaymentType paymentType) {

        // 같은 주문번호 생성 (한 번만!)
        String orderNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" + String.format("%03d", (int)(Math.random() * 1000));

        String orderTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));

        // 각 아이템을 같은 주문번호로 저장
        for (OrderItem item : items) {
            Orders order = Orders.builder()
                    .orderNumber(orderNumber)
                    .userId(userId)
                    .icon(item.getIcon())
                    .option(option)
                    .itemName(item.getName())
                    .category(getCategoryByMenuItem(item.getName()))  // ← 추가!
                    .quantity(item.getQuantity())
                    .price(item.getPrice() * item.getQuantity())
                    .roomNumber(roomNumber)
                    .phoneNumber(phoneNumber)
                    .status("ORDERED")
                    .orderTime(orderTime)
                    .specialRequests(specialRequests)
                    .paymentType(paymentType)
                    .build();

            ordersRepository.save(order);
        }
    }

    @Transactional(readOnly = true)
    public List<OrderGroupDto> getCurrentOrdersGrouped(String roomNumber) {
        List<Orders> orders = ordersRepository.findCurrentOrdersByRoom(roomNumber);

        // 주문번호로 그룹핑
        Map<String, List<Orders>> grouped = orders.stream()
                .collect(Collectors.groupingBy(Orders::getOrderNumber));

        // OrderGroupDto로 변환
        return grouped.entrySet().stream()
                .map(entry -> new OrderGroupDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderGroupDto> getCompletedOrdersGrouped(String roomNumber) {
        List<Orders> orders = ordersRepository.findCompletedOrdersByRoom(roomNumber);

        Map<String, List<Orders>> grouped = orders.stream()
                .collect(Collectors.groupingBy(Orders::getOrderNumber));

        return grouped.entrySet().stream()
                .map(entry -> new OrderGroupDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    // ✅ 카테고리 자동 매핑 메소드 추가
    private String getCategoryByMenuItem(String itemName) {
        if (itemName == null) return "기타";

        // 햄버거
        if (itemName.contains("버거")){
            return "햄버거";
        }
        // 피자
        if (itemName.contains("피자")) {
            return "피자";
        }
        // 주류
        if (itemName.contains("와인") || itemName.contains("칵테일") ||
                itemName.contains("생맥주")) {
            return "주류";
        }
        // 디저트
        if (itemName.contains("케이크") || itemName.contains("아이스크림") ||
                itemName.contains("쿠키")) {
            return "디저트";
        }
        // 음료/커피
        if (itemName.contains("아메리카노") || itemName.contains("카푸치노") || itemName.contains("오렌지 주스")) {
            return "음료/커피";
        }

        return "기타";
    }

    // OrderItem 내부 클래스
    public static class OrderItem {
        private String name;
        private int quantity;
        private int price;
        private String icon;

        public OrderItem() {
        }

        public OrderItem(String name, int quantity, int price, String icon) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
            this.icon = icon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}