package com.jojoldu.book.springboot.domain.order;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;      // 주문번호
    private String userId;           // 주문한 사용자
    private String itemName;         // 메뉴명
    private String category;
    private int quantity;            // 수량
    private int price;               // 가격
    private String roomNumber;       // 객실번호
    private String phoneNumber;      // 연락처
    private String status;           // 상태 (ORDERED, PREPARED, DELIVERING, COMPLETED)
    private String orderTime;        // 주문시간
    private String specialRequests;        // 특별 요청사항
    private String icon;        // 아이콘
    private String option;        // 옵션(룸서비스, 물품 주문)

    // enum 클래스 사용해서 타입 안정성 확보
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private PaymentType paymentType;     // 결제 방식 (IMMEDIATE, DEFERRED 등)


    @Builder
    public Orders(String orderNumber, String userId, String itemName, String category, String option,
                  int quantity, int price, String icon, String roomNumber,
                  String phoneNumber, String status, String orderTime, String specialRequests,
                  PaymentType paymentType) {
        this.orderNumber = orderNumber;
        this.option = option;
        this.userId = userId;
        this.itemName = itemName;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.roomNumber = roomNumber;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.orderTime = orderTime;
        this.icon = icon;
        this.specialRequests = specialRequests;
        this.paymentType = paymentType;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}