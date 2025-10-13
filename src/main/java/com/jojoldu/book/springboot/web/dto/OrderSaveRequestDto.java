package com.jojoldu.book.springboot.web.dto;

import com.jojoldu.book.springboot.domain.orders.Orders;
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

    public Orders toEntity() {
        return Orders.builder()
                .roomNumber(roomNumber)
                .build();
    }
}
