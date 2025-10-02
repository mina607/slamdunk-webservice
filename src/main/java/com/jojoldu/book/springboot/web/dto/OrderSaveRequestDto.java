package com.jojoldu.book.springboot.web.dto;

import com.jojoldu.book.springboot.domain.orders.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderSaveRequestDto {
    private String roomNumber;

    @Builder
    public OrderSaveRequestDto(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Order toEntity() {
        return Order.builder()
                .roomNumber(roomNumber)
                .build();
    }
}
