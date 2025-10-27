package com.jojoldu.book.springboot.web.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class HourlyOrderDto {
    private Integer hour;        // 시간 (0~23)
    private Long foodCount;      // 음식 주문 건수
    private Long productCount;   // 물품 주문 건수
}