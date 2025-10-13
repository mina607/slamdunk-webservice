package com.jojoldu.book.springboot.service;

import com.jojoldu.book.springboot.domain.order.Orders;
import lombok.Getter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderGroupDto {
    private String orderNumber;
    private String orderTime;
    private String status;
    private String roomNumber;
    private String phoneNumber;
    private List<OrderItemDto> items;
    private String totalPrice;  // String으로!

    public OrderGroupDto(String orderNumber, List<Orders> orders) {
        if (orders.isEmpty()) {
            System.out.println("Orders is empty!");  // 디버깅
            return;
        }

        Orders first = orders.get(0);
        this.orderNumber = orderNumber;
        this.orderTime = first.getOrderTime();
        this.status = first.getStatus();
        this.roomNumber = first.getRoomNumber();
        this.phoneNumber = first.getPhoneNumber();

        // 각 주문을 아이템으로 변환
        this.items = orders.stream()
                .map(order -> new OrderItemDto(
                        order.getItemName(),
                        order.getQuantity(),
                        order.getPrice()
                ))
                .collect(Collectors.toList());

        // 총 가격 계산 후 포맷팅
        int total = orders.stream()
                .mapToInt(Orders::getPrice)
                .sum();
        this.totalPrice = String.format("%,d", total);
        System.out.println("Total Price: " + this.totalPrice);
    }

    @Getter
    public static class OrderItemDto {
        private String itemName;
        private int quantity;
        private String price;

        public OrderItemDto(String itemName, int quantity, int price) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.price = String.format("%,d", price);
        }
    }
}