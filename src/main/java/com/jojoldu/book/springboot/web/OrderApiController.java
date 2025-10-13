package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.service.OrderService;
import com.jojoldu.book.springboot.web.dto.OrderSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OrderApiController {

    private final OrderService orderService;

    // 등록하기
    @PostMapping("/api/order")
    public Long save(@RequestBody OrderSaveRequestDto requestDto) {
        return orderService.save(requestDto);
    }
}
