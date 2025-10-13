package com.jojoldu.book.springboot.service;

import com.jojoldu.book.springboot.domain.order.Orders;
import com.jojoldu.book.springboot.domain.order.OrdersRepository;
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
        return ordersRepository.findCurrentOrdersByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Orders> getCompletedOrders(String userId) {
        return ordersRepository.findCompletedOrdersByUserId(userId);
    }
    // 주문 저장 메서드 추가
    @Transactional
    public Long saveOrder(String userId, String itemName, int quantity,
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
                .quantity(quantity)
                .price(price)
                .roomNumber(roomNumber)
                .phoneNumber(phoneNumber)
                .status("ORDERED")  // 초기 상태는 주문접수
                .orderTime(orderTime)
                .build();

        return ordersRepository.save(order).getId();
    }
    @Transactional
    public void saveMultipleOrders(String userId, List<OrderItem> items,
                                   String roomNumber, String phoneNumber) {

        // 같은 주문번호 생성 (한 번만!)
        String orderNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" + String.format("%03d", (int)(Math.random() * 1000));

        String orderTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));

        // 각 아이템을 같은 주문번호로 저장
        for (OrderItem item : items) {
            Orders order = Orders.builder()
                    .orderNumber(orderNumber)  // 같은 번호!
                    .userId(userId)
                    .itemName(item.getName())
                    .quantity(item.getQuantity())
                    .price(item.getPrice() * item.getQuantity())
                    .roomNumber(roomNumber)
                    .phoneNumber(phoneNumber)
                    .status("ORDERED")
                    .orderTime(orderTime)
                    .build();

            ordersRepository.save(order);
        }
    }

    @Transactional(readOnly = true)
    public List<OrderGroupDto> getCurrentOrdersGrouped(String userId) {
        List<Orders> orders = ordersRepository.findCurrentOrdersByUserId(userId);

        // 주문번호로 그룹핑
        Map<String, List<Orders>> grouped = orders.stream()
                .collect(Collectors.groupingBy(Orders::getOrderNumber));

        // OrderGroupDto로 변환
        return grouped.entrySet().stream()
                .map(entry -> new OrderGroupDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderGroupDto> getCompletedOrdersGrouped(String userId) {
        List<Orders> orders = ordersRepository.findCompletedOrdersByUserId(userId);

        Map<String, List<Orders>> grouped = orders.stream()
                .collect(Collectors.groupingBy(Orders::getOrderNumber));

        return grouped.entrySet().stream()
                .map(entry -> new OrderGroupDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    // OrderItem 내부 클래스
    public static class OrderItem {
        private String name;
        private int quantity;
        private int price;

        public OrderItem() {}

        public OrderItem(String name, int quantity, int price) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public int getPrice() { return price; }
        public void setPrice(int price) { this.price = price; }
    }
}