package com.jojoldu.book.springboot.domain.order;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;      // 주문번호
    private String userId;           // 주문한 사용자
    private String itemName;         // 메뉴명
    private int quantity;            // 수량
    private int price;               // 가격
    private String roomNumber;       // 객실번호
    private String phoneNumber;      // 연락처
    private String status;           // 상태 (ORDERED, COOKING, DELIVERING, COMPLETED)
    private String orderTime;        // 주문시간
    private String icon;        // 아이콘


    @Builder
    public Orders(String orderNumber, String userId, String itemName,
                  int quantity, int price, String icon, String roomNumber,
                  String phoneNumber, String status, String orderTime) {
        this.orderNumber = orderNumber;
        this.userId = userId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.roomNumber = roomNumber;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.orderTime = orderTime;
        this.icon = icon;
    }


}