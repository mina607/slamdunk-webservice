package com.jojoldu.book.springboot.service;

import com.jojoldu.book.springboot.domain.orders.OrderRepository;
import com.jojoldu.book.springboot.web.dto.OrderSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public Long save(OrderSaveRequestDto requestDto) {
        return orderRepository.save(requestDto.toEntity()).getId();
    }
}
